package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.reduxrobotics.sensors.canandmag.Canandmag;
import com.reduxrobotics.sensors.canandmag.CanandmagSettings;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;;

public class Arm extends SubsystemBase {

  private TalonFX motor;
  private TalonFXConfiguration config;
  private Canandmag canandmag;
  private CanandmagSettings canandmagSettings;
  private PIDController controller;

  public Arm() {
    motor = new TalonFX(Constants.ID.ARM_TALONFX_ID);
    config = new TalonFXConfiguration();
    canandmag = new Canandmag(Constants.ID.ARM_ENCODER_ID);
    canandmagSettings = new CanandmagSettings();
    canandmagSettings.setInvertDirection(true);

    controller = new PIDController(
      Constants.ArmConstants.P,
      Constants.ArmConstants.I,
      Constants.ArmConstants.D);
    controller.setTolerance(Constants.ArmConstants.TOLERANCE);

    config.CurrentLimits.SupplyCurrentLimit = Constants.ArmConstants.CURRENT_LIMIT;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    motor.getConfigurator().apply(config);
    motor.setNeutralMode(NeutralModeValue.Brake);
    canandmag.setSettings(canandmagSettings);
    canandmag.setPosition(0);
  }

  private double feedforward(double position) {
    double halfPosition = 0; // this is what the position reads when the arm is straight up
    double kG = 0; // this is js the max feedforward, that theoeretically should be needed to hold the arm up in the .25 progression state

    double currentPosition = canandmag.getPosition();
    double progression = currentPosition/halfPosition * 2;
    double progressionRads = progression * 2 *  Math.PI;
    double feedforward = Math.abs(kG * Math.sin(progressionRads));
    return feedforward;
  }

  public  void goTowardsDesiredPosition(double desiredPosition) {
    double currentPosition = canandmag.getPosition();
    double output = controller.calculate(currentPosition, desiredPosition);
    motor.set(output + feedforward(currentPosition));
    SmartDashboard.putNumber("Arm Desired Position", desiredPosition);
  }

  public void setRawSpeed(double desiredSpeed) {
    motor.set(desiredSpeed);
  }

  public void setSpeed(double desiredSpeed) {
    motor.set(desiredSpeed + feedforward(canandmag.getPosition()));
  }

  public boolean atSetpoint() {
    return controller.atSetpoint();
  }

  public void stop() {
    motor.set(0 + feedforward(canandmag.getPosition()));
  }

  @Override
  public void periodic() {    
    SmartDashboard.putBoolean("Arm at Setpoint", controller.atSetpoint());
    SmartDashboard.putNumber("Arm Forward Limit", Constants.ArmConstants.FORWARD_LIMIT);
    SmartDashboard.putNumber("Arm Reverse Limit", Constants.ArmConstants.REVERSE_LIMIT);
    SmartDashboard.putNumber("Arm Velocity", canandmag.getVelocity());
    SmartDashboard.putNumber("Arm Motor Output", motor.get());
    SmartDashboard.putNumber("Arm Position", canandmag.getPosition());
    SmartDashboard.putNumber("Arm Supply Current", motor.getSupplyCurrent().getValueAsDouble());
  }
  
}
