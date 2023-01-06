// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.GamePadButtons;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.RIO_Channels_CAN_MOTOR;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.CompressorSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.ImuSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Leds;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.TowerSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // Talon and Pigeon needed for subsystems defined here...
  public final static TalonSRX  talon1 = new TalonSRX(RIO_Channels_CAN_MOTOR.INTAKE);
  public final static PigeonIMU  pidgey = new PigeonIMU(talon1);
  // The robot's subsystems and commands are defined here...
  public final static ImuSubsystem imuSubsystem = new ImuSubsystem(pidgey);
  public final static LimeLight limelight = new LimeLight();
  public final static DriveSubsystem driveTrain = new DriveSubsystem(imuSubsystem);
  public final static Leds leds = new Leds();
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  public final static Intake intake = new Intake(talon1);
  public final static TowerSubsystem tower = new TowerSubsystem();
  public final static Launcher launcher = new Launcher();
  public final static CompressorSubsystem compressor = new CompressorSubsystem();


 // The driver's controller
 XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);


  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

        //limelight.setDefaultCommand(new InstantCommand(limelight::ledOff, limelight));

          leds.setDefaultCommand(
              new RunCommand(() -> leds.rainbow(), leds)
          );

        // Set the default drive command to split-stick arcade drive
        driveTrain.setDefaultCommand(
          // A split-stick arcade command, with forward/backward controlled by the left
          // hand, and turning controlled by the right.
          new RunCommand(
              () ->
                  driveTrain.drive(
                    -m_driverController.getLeftY()
                    * DriveConstants.kMaxSpeedMetersPerSecond,
                      -m_driverController.getLeftX()
              * DriveConstants.kMaxSpeedMetersPerSecond,
                     // -m_driverController.getRightX()
                     -(m_driverController.getRightTriggerAxis()-m_driverController.getLeftTriggerAxis())
              * DriveConstants.kMaxSpeedMetersPerSecond,
                      
                      true), driveTrain));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    new JoystickButton(m_driverController, GamePadButtons.Start)
    .whileActiveOnce(new InstantCommand(driveTrain::resetAll, driveTrain));

    new JoystickButton(m_driverController, GamePadButtons.Y)
    .whenPressed(new InstantCommand(limelight::ledPipeline, limelight))
    .whenPressed(new InstantCommand(limelight::setPipelineThree, limelight))
    .whileHeld(new RunCommand(() -> driveTrain.limeLightAim(
                    -m_driverController.getLeftY()
                    * DriveConstants.kMaxSpeedMetersPerSecond,
                      -m_driverController.getLeftX()
              * DriveConstants.kMaxSpeedMetersPerSecond), driveTrain))
    .whenReleased(new InstantCommand(limelight::setPipelineZero, limelight));

    new JoystickButton(m_driverController, GamePadButtons.X)
    .whenHeld(new RunCommand(intake::intakeDownRoll, intake))
    .whenHeld(new RunCommand(tower::autoTowerStow, tower))
    .whenReleased(new RunCommand (intake::intakeUpStop, intake))
    .whenReleased(new RunCommand(tower::towerStop, tower));

    
    new JoystickButton(m_driverController, GamePadButtons.A)
    //.whenHeld(new RunCommand(intake::intakeDownRoll, intake))
    .whenHeld(new RunCommand(tower::autoTowerStow, tower))
    //.whenReleased(new RunCommand (intake::intakeUpStop, intake))
    .whenReleased(new RunCommand(tower::towerStop, tower));

    new JoystickButton(m_driverController, GamePadButtons.B)
    .whenHeld(new RunCommand(intake::intakeRollersReverse, intake))
    .whenHeld(new RunCommand(tower::ejectBalls, tower))
    .whenReleased(new RunCommand (intake::intakeUpStop, intake))
    .whenReleased(new RunCommand(tower::towerStop, tower));




    new JoystickButton(m_driverController, GamePadButtons.RB)
    .whenHeld(new RunCommand(tower::feedBallsToShooter, tower))
    .whenReleased(new RunCommand (tower::towerStop, tower));

    new POVButton(m_driverController, GamePadButtons.Up)
    .whenPressed(new RunCommand(launcher::longTarmacShoot, launcher))
    .whenPressed(new RunCommand(leds::red, leds));

    
    new POVButton(m_driverController, GamePadButtons.Right)
      .whenPressed(new RunCommand(launcher::midTarmacShoot, launcher))
      .whenPressed(new RunCommand(leds::purple, leds));

    new POVButton(m_driverController, GamePadButtons.Down)
      .whenPressed(new RunCommand(launcher::fenderHighShoot, launcher))
      .whenPressed(new RunCommand(leds::green, leds));
    
    new POVButton(m_driverController, GamePadButtons.Left)
      .whenPressed(new RunCommand(launcher::stopFlyWheel, launcher))
      .whenReleased(new RunCommand(leds::rainbow, leds));




}




  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
