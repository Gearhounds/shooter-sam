// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import edu.wpi.first.wpilibj.Solenoid;

import edu.wpi.first.wpilibj.Joystick;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  WPI_TalonFX leftmotor1 = new WPI_TalonFX(5, "rio");
  WPI_TalonFX leftmotor2 = new WPI_TalonFX(4, "rio");
  WPI_TalonFX Rightmotor1 = new WPI_TalonFX(1, "rio");
  WPI_TalonFX Rightmotor2 = new WPI_TalonFX(2, "rio");

  VictorSP intake = new VictorSP(0);

  MotorController m_frontLeft = new WPI_TalonFX(5);
  MotorController m_rearLeft = new WPI_TalonFX(4);
  MotorControllerGroup m_left = new MotorControllerGroup(m_frontLeft, m_rearLeft);

  MotorController m_frontRight = new WPI_TalonFX(1);
  MotorController m_rearRight = new WPI_TalonFX(2);
  MotorControllerGroup m_right = new MotorControllerGroup(m_frontRight, m_rearRight);
  DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);

  //DoubleSolenoid shooterSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  Compressor robotCompressor = new Compressor(PneumaticsModuleType.CTREPCM);
  //Solenoid shooterSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 4);
  Solenoid shooterSolenoid = new Solenoid(1, PneumaticsModuleType.CTREPCM, 4);

  //private final XboxController m_driverController = new XboxController(0);

  private final Joystick m_driverController = new Joystick(0);

  TalonFX m_leftShooter = new TalonFX(6);
  TalonFX m_rightShooter = new TalonFX(3);
  
  CANSparkMax feed = new CANSparkMax(10, MotorType.kBrushless);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_right.setInverted(true);
    m_rightShooter.setInverted(true);

    //Solenoid.set(false);
    robotCompressor.enableDigital();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    m_leftShooter.set(ControlMode.PercentOutput, 0);
    m_rightShooter.set(ControlMode.PercentOutput, 0);
    

    /* 
    double m_LeftTriggerPos = m_driverController.getLeftTriggerAxis();
    double m_RightTriggerPos = m_driverController.getRightTriggerAxis();
    if(m_LeftTriggerPos != 0){
      if(m_RightTriggerPos != 0){
        m_robotDrive.stopMotor();
      }else{
        m_robotDrive.arcadeDrive(-m_LeftTriggerPos, -m_driverController.getLeftX());
      }
    }else if(m_RightTriggerPos != 0){
      m_robotDrive.arcadeDrive(m_RightTriggerPos, -m_driverController.getLeftX());
    }else{
      m_robotDrive.arcadeDrive(0.0, -m_driverController.getLeftX());
    }
    */
    double throttle = (-m_driverController.getThrottle() + 1)/2;
    double driveOutPut = -m_driverController.getY() * throttle;
    double turnOutPut = -m_driverController.getX() * throttle;

    m_robotDrive.arcadeDrive(driveOutPut, turnOutPut);

    if (m_driverController.getRawButton(6)) {
      m_leftShooter.set(ControlMode.PercentOutput, -.35);
      m_rightShooter.set(ControlMode.PercentOutput, -.35);
    }

    if (m_driverController.getRawButton(5)) {
      m_leftShooter.set(ControlMode.PercentOutput, -.125);
      m_rightShooter.set(ControlMode.PercentOutput, -.125);
    }
    //shooterSolenoid.set(Value.kOff);
    shooterSolenoid.set(false);
    if (m_driverController.getRawButton(1)) {
      //shooterSolenoid.set(Value.kForward);
      shooterSolenoid.set(true);
      //m_rightShooter.set(ControlMode.PercentOutput, -.20);
    }
    
    if (m_driverController.getRawButton(11)) {
      intake.set(1);
    } else {
      intake.set(0);
    }

    if (m_driverController.getRawButton(12)){
      feed.set(.35);
    }else{
      feed.set(0);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
