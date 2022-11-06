// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.ImuSubsystem;
import frc.robot.subsystems.Leds;
import frc.robot.subsystems.LimeLight;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // Talon and Pigeon needed for subsystems defined here...
  public final static TalonSRX  talon1 = new TalonSRX(DriveConstants.kIntakeTalonPort);
  public final static PigeonIMU  pidgey = new PigeonIMU(talon1);
  // The robot's subsystems and commands are defined here...
  public final static ImuSubsystem imuSubsystem = new ImuSubsystem(pidgey);
  public final static LimeLight limelight = new LimeLight();
  public final static DriveSubsystem driveTrain = new DriveSubsystem(imuSubsystem);
  public final static Leds leds = new Leds();
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();


 // The driver's controller
 XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);


  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

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
                      -m_driverController.getRightX()
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

    new JoystickButton(m_driverController, 4)
    .whenPressed(new InstantCommand(limelight::ledPipeline, limelight))
    .whenPressed(new InstantCommand(limelight::setPipelineThree, limelight))
    .whileHeld(new RunCommand(() -> driveTrain.limeLightAim(
                    -m_driverController.getLeftY()
                    * DriveConstants.kMaxSpeedMetersPerSecond,
                      -m_driverController.getLeftX()
              * DriveConstants.kMaxSpeedMetersPerSecond), driveTrain))
    .whenReleased(new InstantCommand(limelight::setPipelineZero, limelight));
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