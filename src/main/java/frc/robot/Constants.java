package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.util.COTSTalonFXSwerveConstants;
import frc.lib.util.SwerveModuleConstants;

public final class Constants {

    public static final class ID {

        // SWERVE ('cani' bus) --------------------------

        public static final int PIDGEON_ID = 0;
        public static final int FRONT_LEFT_DRIVE_TALONFX_ID = 1;
        public static final int FRONT_LEFT_CANCODER_ID = 1;
        public static final int FRONT_LEFT_ANGLE_TALONFX_ID = 2;
        public static final int FRONT_RIGHT_CANCODER_ID = 2;
        public static final int FRONT_RIGHT_DRIVE_TALONFX_ID = 3;
        public static final int BACK_RIGHT_CANCODER_ID = 3;
        public static final int FRONT_RIGHT_ANGLE_TALONFX_ID = 4;
        public static final int BACK_LEFT_CANCODER_ID = 4;
        public static final int BACK_RIGHT_DRIVE_TALONFX_ID = 5;
        public static final int BACK_RIGHT_ANGLE_TALONFX_ID = 6;
        public static final int BACK_LEFT_DRIVE_TALONFX_ID = 7;
        public static final int BACK_LEFT_ANGLE_TALONFX_ID = 8;

        // SUPERSTRUCTURE ('rio' bus) --------------------------

        public static final int END_EFFECTOR_TALONFX_ID = 9;
        public static final int ELEVATOR_MASTER_TALONFX_ID = 10;
        public static final int ELEVATOR_FOLLOWER_TALONFX_ID = 11;
        public static final int INTAKE_TALONFX_ID = 12;
        public static final int ELBOW_TALONFX_ID = 13;
        public static final int ARM_ENCODER_ID = 14;
        public static final int ELEVATOR_ENCODER_ID = 15;
        public static final int INTAKE_ENCODER_ID = 16;
        public static final int ARM_TALONFX_ID = 17;
    }

    public static final class ControlConstants {
        public static final int DRIVER_PORT = 0;
        public static final int OPERATOR_PORT = 1;
        public static final double STICK_DEADBAND = 0.05;
        public static final int OPERATOR_KEYPAD_PORT = 2;
        public static final double UPDATE_INTERVAL = 0.05;
    }

    public static final class NotificationConstants {
        public static final double BATTERY_LOW_VOLTAGE = 11.5;
        public static final double BATTERY_LOW_DURATION = 5.0;
    }

    public static final class EndEffectorConstants {
        public static final int MOTOR_ID = 9;
        public static final double CURRENT_LIMIT = 30;
        public static final double CORAL_INTAKE_SPEED = -.1;
        public static final double CORAL_OUTTAKE_SPEED = .3;
        public static final double ALGAE_INTAKE_SPEED = -1;
        public static final double ALGAE_OUTTAKE_SPEED = 1;
    }

    public static final class ElevatorConstants {
        public static final int MASTER_ID = 10;
        public static final int FOLLOWER_ID = 11;
        public static final int CURRENT_LIMIT = 40;
        public static final int ENCODER_ID = 15;
        public static final double P = 1;
        public static final double I = .1;
        public static final double D = 0;
        public static final double G = 0;
        public static final double S = 0;
        public static final double TOLERANCE = .04;
        public static final double FORWARD_LIMIT = 5.2327880859375;
        public static final double REVERSE_LIMIT = 0;
    }

    public static final class ClimberConstants {
        public static final int MOTOR_ID = 12;
        public static final double CURRENT_LIMIT = 30;
        public static final double CLIMBER_SPEED = .5;
    }

    public static final class ArmConstants{
        public static final int MOTOR_ID = 13;
        public static final int CURRENT_LIMIT = 30;
        public static final double P = 1;
        public static final double I = .5;
        public static final double D = 0;
        public static final double TOLERANCE = .01;
        public static final double FORWARD_LIMIT = 2.2;
        public static final double REVERSE_LIMIT = .0;
        public static final int ENCODER_ID = 17;
        public static final double G = 0;
    }

    public static final class Swerve {
        public static final int pigeonID = 0;

        public static final COTSTalonFXSwerveConstants chosenModule =
        COTSTalonFXSwerveConstants.SDS.MK4i.KrakenX60(COTSTalonFXSwerveConstants.SDS.MK4i.driveRatios.LMatthew);
     
        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(24.75);
        public static final double wheelBase = Units.inchesToMeters(24.75);
        public static final double wheelCircumference = chosenModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenModule.driveGearRatio;
        public static final double angleGearRatio = chosenModule.angleGearRatio;

        /* Motor Inverts */
        public static final InvertedValue angleMotorInvert = chosenModule.angleMotorInvert;
        public static final InvertedValue driveMotorInvert = chosenModule.driveMotorInvert;

        /* Angle Encoder Invert */
        public static final SensorDirectionValue cancoderInvert = chosenModule.cancoderInvert;

        /* Swerve Current Limiting */
        public static final int angleCurrentLimit = 25;
        public static final int angleCurrentThreshold = 40;
        public static final double angleCurrentThresholdTime = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveCurrentLimit = 35;
        public static final int driveCurrentThreshold = 60;
        public static final double driveCurrentThresholdTime = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = 10.0;
        public static final double angleKI = 0.0;
        public static final double angleKD = 0.0;

        /* Drive Motor PID Values*/
        public static final double driveKP = 1;
        public static final double driveKI = 0.01;
        public static final double driveKD = 0.01;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values From SYSID */
        public static final double driveKS = 0.53114;
        public static final double driveKV = 2.3423;
        public static final double driveKA = 0.12817;

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 4.5;
        /** Radians per Second */
        public static final double maxAngularVelocity = 10.0;

        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Brake;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;

        /* Module Specific Constants */
        /* Front Left Module - Module 0 */
        public static final class Mod0 {
            public static final int driveMotorID = 1;
            public static final int angleMotorID = 2;
            public static final int canCoderID = 1;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-57.656250);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Front Right Module - Module 1 */
        public static final class Mod1 {
            public static final int driveMotorID = 3;
            public static final int angleMotorID = 4;
            public static final int canCoderID = 2;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(163.564453);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Left Module - Module 2 */
        public static final class Mod2 {
            public static final int driveMotorID = 5;
            public static final int angleMotorID = 6;
            public static final int canCoderID = 3;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(66.533203);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Right Module - Module 3 */
        public static final class Mod3 {
            public static final int driveMotorID = 7;
            public static final int angleMotorID = 8;
            public static final int canCoderID = 4;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-165.673828);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
    }

    public static final class AutoConstants {
        public static final double kMaxSpeedMetersPerSecond = 3.85;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3.85;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = .12;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;
    
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }

}