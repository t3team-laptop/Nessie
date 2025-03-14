package frc.robot.commands.swerve;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class FastMode extends Command {

  private Swerve swerve;

  public FastMode(Swerve swerve) {
    this.swerve = swerve;
    addRequirements(swerve);
  }

  @Override
  public void initialize() {
    swerve.setSpeedMultiplier(1.0);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
