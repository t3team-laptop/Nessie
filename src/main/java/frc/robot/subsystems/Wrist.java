package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Wrist extends SubsystemBase {

  private SparkMax motor;
  private SparkMaxConfig motorConfig;
  private SparkLimitSwitch forwardLimitSwitch;
  private SparkLimitSwitch reverseLimitSwitch;
  private PIDController pidController;
  private RelativeEncoder encoder;

  public Wrist() {
    motor = new SparkMax(Constants.WristConstants.wristMotorID, MotorType.kBrushless);
    forwardLimitSwitch = motor.getForwardLimitSwitch();
    reverseLimitSwitch = motor.getReverseLimitSwitch();
    encoder = motor.getEncoder();

    motorConfig = new SparkMaxConfig();

    motorConfig.idleMode(IdleMode.kBrake);

    // Enable limit switches to stop the motor when they are closed
    motorConfig.limitSwitch
        .forwardLimitSwitchType(Type.kNormallyOpen)
        .forwardLimitSwitchEnabled(true)
        .reverseLimitSwitchType(Type.kNormallyOpen)
        .reverseLimitSwitchEnabled(true);

    // Set the soft limits to stop the motor at -50 and 50 rotations
    motorConfig.softLimit
        .forwardSoftLimit(Constants.WristConstants.forwardSoftLimit)
        .forwardSoftLimitEnabled(true)
        .reverseSoftLimit(Constants.WristConstants.reverseSoftLimit)
        .reverseSoftLimitEnabled(true);
      
    motorConfig.smartCurrentLimit(Constants.WristConstants.wristCurrentLimit);

    motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    pidController = new PIDController(
      Constants.WristConstants.p, 
      Constants.WristConstants.i, 
      Constants.WristConstants.d);
    pidController.setTolerance(Constants.WristConstants.tolerance);

    encoder.setPosition(0);
  }

  public void setRotation(double desiredPosition) {
    SmartDashboard.putNumber("Wrist Setpoint", desiredPosition);
    motor.set(pidController.calculate(encoder.getPosition(), desiredPosition));
  }

  public void setSpeed(double speed) {
    motor.set(speed);
  }

  public void stop() {
    motor.stopMotor();
  }

  public boolean atSetpoint() {
    return pidController.atSetpoint();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Wrist Position", encoder.getPosition());
    SmartDashboard.putBoolean("Wrist at Setpoint", atSetpoint());
    SmartDashboard.putBoolean("Wrist at Forward Limit", forwardLimitSwitch.isPressed());
    SmartDashboard.putBoolean("Wrist at Reverse Limit", reverseLimitSwitch.isPressed());
  }

}