package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.lib.Elastic;
import frc.lib.Elastic.Notification;
import frc.lib.Elastic.Notification.NotificationLevel;
import frc.robot.autos.AutoCoralIntake;
import frc.robot.autos.AutoCoralOuttake;
import frc.robot.commands.Climber.ClimberDown;
import frc.robot.commands.Climber.ClimberUp;
import frc.robot.commands.EndEffector.AlgaeIntake;
import frc.robot.commands.EndEffector.AlgaeOuttake;
import frc.robot.commands.EndEffector.CoralIntake;
import frc.robot.commands.EndEffector.CoralOuttake;
import frc.robot.commands.Manual.ManualElbow;
import frc.robot.commands.Manual.ManualElevator;
import frc.robot.commands.Manual.ManualEndEffector;
import frc.robot.commands.Manual.ManualWrist;
import frc.robot.commands.Positioning.SetAlgaeBottomPosition;
import frc.robot.commands.Positioning.SetAlgaeShootPosition;
import frc.robot.commands.Positioning.SetAlgaeTopPosition;
import frc.robot.commands.Positioning.SetCagePosition;
import frc.robot.commands.Positioning.SetGroundAlgaePosition;
import frc.robot.commands.Positioning.SetL1Position;
import frc.robot.commands.Positioning.SetL2Position;
import frc.robot.commands.Positioning.SetL3Position;
import frc.robot.commands.Positioning.SetL4Position;
import frc.robot.commands.Positioning.SetProcessorPosition;
import frc.robot.commands.Positioning.SetSourcePosition;
import frc.robot.commands.Positioning.SetStowPosition;
import frc.robot.commands.Swerve.*;
import frc.robot.subsystems.*;

public class RobotContainer {

    /* Controllers */
    private final CommandXboxController driver = new CommandXboxController(Constants.ControlConstants.DRIVER_PORT);
    private final CommandXboxController operator = new CommandXboxController(Constants.ControlConstants.OPERATOR_PORT);
    @SuppressWarnings("unused")
    private final GenericHID operatorKeypad = new GenericHID(Constants.ControlConstants.OPERATOR_KEYPAD_PORT);

    /* Drive Controls */
    private final int leftY = XboxController.Axis.kLeftY.value;
    private final int leftX = XboxController.Axis.kLeftX.value;
    private final int rightX = XboxController.Axis.kRightX.value;
    private final int rightTrigger = XboxController.Axis.kRightTrigger.value;
    private final int leftTrigger = XboxController.Axis.kLeftTrigger.value;
    private final int rightY = XboxController.Axis.kRightY.value;

    /* Subsystems */
    private final Swerve swerve = new Swerve();
    private final Wrist wrist = new Wrist(this);
    private final Elbow elbow = new Elbow(this);
    private final Elevator elevator = new Elevator(this);
    private final EndEffector endEffector = new EndEffector();
    private final Climber climber = new Climber();

    /* Commands */
    private final ClimberUp climberUp;
    private final ClimberDown climberDown;
    private final CoralIntake coralIntake;
    private final CoralOuttake coralOuttake;
    private final AlgaeIntake algaeIntake;
    private final AlgaeOuttake algaeOuttake;
    private final SetAlgaeBottomPosition setAlgaeBottomPosition;
    private final SetAlgaeShootPosition setAlgaeShootPosition;
    private final SetAlgaeTopPosition setAlgaeTopPosition;
    private final SetL1Position setL1Position;
    private final SetL2Position setL2Position;
    private final SetL3Position setL3Position;
    private final SetL4Position setL4Position;
    private final SetGroundAlgaePosition setGroundAlgaePosition;
    private final SetStowPosition setStowPosition;
    private final SetSourcePosition setSourcePosition;
    private final SetProcessorPosition setProcessorPosition;
    private final SetCagePosition setCagePosition;
    private final AutoCoralIntake autoCoralIntake;
    private final AutoCoralOuttake autoCoralOuttake;
    private final FastMode fastMode;
    private final SlowMode slowMode;

    /* Autos */
    private final SendableChooser<Command> autoChooser;

    /* Game */
    public boolean isAlgae = false;
    public boolean manual = false;

    public RobotContainer() {

        swerve.setDefaultCommand(
            new TeleopSwerve(
                swerve, 
                () -> -driver.getRawAxis(leftY), 
                () -> -driver.getRawAxis(leftX), 
                () -> -driver.getRawAxis(rightX), 
                
                () -> driver.leftBumper().getAsBoolean()
            )
        );

        if(manual) {

            elevator.setDefaultCommand(
                new ManualElevator(
                    elevator,
                    () -> -(operator.getRawAxis(leftTrigger) - operator.getRawAxis(rightTrigger))
                )
            );

            elbow.setDefaultCommand(
                new ManualElbow(
                    elbow,
                    () -> operator.getRawAxis(rightY)
                )
            );

            wrist.setDefaultCommand(
                new ManualWrist(
                    wrist,
                    () -> -operator.getRawAxis(leftY)
                )
            );

            endEffector.setDefaultCommand(
                new ManualEndEffector(
                    endEffector,
                    () -> operator.getRawAxis(leftX)
                )
            );
        }

        climberUp = new ClimberUp(climber);
        climberUp.addRequirements(climber);
        climberDown = new ClimberDown(climber);
        climberDown.addRequirements(climber);
        algaeIntake = new AlgaeIntake(endEffector);
        algaeIntake.addRequirements(endEffector);
        algaeOuttake = new AlgaeOuttake(endEffector);
        algaeOuttake.addRequirements(endEffector);
        coralIntake = new CoralIntake(endEffector);
        coralIntake.addRequirements(endEffector);
        coralOuttake = new CoralOuttake(endEffector);
        coralOuttake.addRequirements(endEffector);
        setAlgaeBottomPosition = new SetAlgaeBottomPosition(elevator, elbow, wrist);
        setAlgaeBottomPosition.addRequirements(elevator, elbow, wrist);
        setAlgaeShootPosition = new SetAlgaeShootPosition(elevator, elbow, wrist);
        setAlgaeShootPosition.addRequirements(elevator, elbow, wrist);
        setAlgaeTopPosition = new SetAlgaeTopPosition(elevator, elbow, wrist);
        setAlgaeTopPosition.addRequirements(elevator, elbow, wrist);
        setL1Position = new SetL1Position(elevator, elbow, wrist);
        setL1Position.addRequirements(elevator, elbow, wrist);
        setL2Position = new SetL2Position(elevator, elbow, wrist);
        setL2Position.addRequirements(elevator, elbow, wrist);
        setL3Position = new SetL3Position(elevator, elbow, wrist);
        setL3Position.addRequirements(elevator, elbow, wrist);
        setL4Position = new SetL4Position(elevator, elbow, wrist);
        setL4Position.addRequirements(elevator, elbow, wrist);
        setGroundAlgaePosition = new SetGroundAlgaePosition(elevator, elbow, wrist);
        setGroundAlgaePosition.addRequirements(elevator, elbow, wrist);
        setStowPosition = new SetStowPosition(elevator, elbow, wrist);
        setStowPosition.addRequirements(elevator, elbow, wrist);
        setSourcePosition = new SetSourcePosition(elevator, elbow, wrist);
        setSourcePosition.addRequirements(elevator, elbow, wrist);
        setProcessorPosition = new SetProcessorPosition(elevator, elbow, wrist);
        setProcessorPosition.addRequirements(elevator, elbow, wrist);
        setCagePosition = new SetCagePosition(elevator, elbow, wrist);
        setCagePosition.addRequirements(elevator, elbow, wrist);
        autoCoralIntake = new AutoCoralIntake(endEffector);
        autoCoralIntake.addRequirements(endEffector);
        autoCoralOuttake = new AutoCoralOuttake(endEffector);
        autoCoralOuttake.addRequirements(endEffector);
        fastMode = new FastMode(swerve);
        fastMode.addRequirements(swerve);
        slowMode = new SlowMode(swerve);
        slowMode.addRequirements(swerve);

        NamedCommands.registerCommand("Go to Source Position", setSourcePosition);
        NamedCommands.registerCommand("Go to L1 Position", setL1Position);
        NamedCommands.registerCommand("Go to L2 Position", setL2Position);
        NamedCommands.registerCommand("Go to L3 Position", setL3Position);
        NamedCommands.registerCommand("Go to L4 Position", setL4Position);
        NamedCommands.registerCommand("Go to Stow Position", setStowPosition);
        NamedCommands.registerCommand("Go to Processor Position", setProcessorPosition);
        NamedCommands.registerCommand("Go to Ground Algae Position", setGroundAlgaePosition);
        NamedCommands.registerCommand("Go to Algae Top Position", setAlgaeTopPosition);
        NamedCommands.registerCommand("Go to Algae Bottom Position", setAlgaeBottomPosition);
        NamedCommands.registerCommand("Go to Cage Position", setCagePosition);
        NamedCommands.registerCommand("Coral Intake", autoCoralIntake);
        NamedCommands.registerCommand("Coral Outtake", autoCoralOuttake);

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureButtonBindings();
    }

    private void configureButtonBindings() { 
        /* --- Driver --- */
        driver.y().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
        driver.leftTrigger().whileTrue(climberDown);
        driver.rightTrigger().whileTrue(climberUp);
        driver.a().onTrue(fastMode);
        driver.b().onTrue(slowMode);

        /* --- Operator  --- */
        operator.rightBumper().whileTrue(coralOuttake);
        operator.leftBumper().whileTrue(coralIntake);

        operator.a().onTrue(setL1Position);
        operator.b().onTrue(setL2Position);
        operator.y().onTrue(setL3Position);
        operator.x().onTrue(setL4Position);

        operator.povUp().onTrue(setSourcePosition);
        operator.povDown().onTrue(setStowPosition);

        operator.povRight().onTrue(new InstantCommand(() -> elbow.adjustOffset(.01)));
        operator.povLeft().onTrue(new InstantCommand(() -> elbow.adjustOffset(-.01)));

        // /* --- Operator Keypad  --- */
        // new JoystickButton(operatorKeypad, 1).onTrue(setL1Position);
        // new JoystickButton(operatorKeypad, 2).onTrue(setL2Position);
        // new JoystickButton(operatorKeypad, 3).onTrue(setL3Position);
        // new JoystickButton(operatorKeypad, 4).onTrue(setL4Position);
        // //new JoystickButton(operatorKeypad, 5).onTrue(new SetDesiredPosition(elevator, elbow, wrist, Constants.PositionConstants.PROCESSOR));
        // new JoystickButton(operatorKeypad, 6).onTrue(setCagePosition);
        // new JoystickButton(operatorKeypad, 7).onTrue(setStowPosition);
        // new JoystickButton(operatorKeypad, 8).onTrue(setAlgaeTopPosition);
        // new JoystickButton(operatorKeypad, 9).onTrue(setAlgaeBottomPosition);
        // new JoystickButton(operatorKeypad, 10).onTrue(setSourcePosition);
        // //new JoystickButton(operatorKeypad, 11).onTrue(new SetDesiredPosition(elevator, elbow, wrist, Constants.PositionConstants.GROUND_ALGAE));
        // new JoystickButton(operatorKeypad, 12).onTrue(new InstantCommand(() -> swapControlMode()));

        
    }

    public void swapControlMode() {
        manual = !manual;
        Elastic.sendNotification(new Notification(NotificationLevel.INFO, "Control Mode Swapped", manual ? "Current Mode: Manual" : "Current Mode: Auto"));
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

}

/*
 * Hey future T3 :) Before I leave I just wanted to thank 
 * this team for giving me the experiences it did.
 * I had no idea I was going to fall in love with making
 * things out of my own hands and watching them come to life.
 * But robotics showed me that, and I will forever be grateful
 * for it. I hope that as you guys code, build, and design
 * for this season you enjoy it as much as I always did. 
 * I know it's cheesy but please don't forget to stop 
 * and smell the roses. Remember why you are doing what you 
 * are doing. Even though things suck when stuff doesn't go
 * as planned, it's a part of the learning process and it 
 * makes you a smarter person as you adapt to it. I know we
 * can't always win and that's okay. You're learning. And that's
 * what matters. I don't think I can even imagine a high school 
 * experience without T3 in it. I devoted 4 years of my life to 
 * this crap and I have absolutely no regrets. As someone who has 
 * been programming for the team for the past few years I know
 * that it can be alot sometimes, but don't worry. Just do your
 * best with the knowledge you have. Also, I wouldn't have done
 * anything other than be the programmer for this team and I
 * loved that I did it. Thank you to all of the people that made
 * my robotics experiences so much better by just being in it.
 * Special shoutouts to: my favorite co-captains Anshu and Ryan,
 * Carrie my favorite coding partner, Anika the little sister
 * I've always wanted, and last but most certainly not least, 
 * Mr. Ware and Mr.Garren for enabling me to have such an
 * amazing educational experience with robotics. 
 * 
 * Thank you for everything,
 * Akshita Santra 
 */