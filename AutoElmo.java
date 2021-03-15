package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="ElmoAuto", group="Pushbot")

public class ElmoAuto extends LinearOpMode {

    /* Declare OpMode members. */
    //HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
     int red = 0;
    int green = 0;
    int blue = 0;
    int red2 = 0;
    int green2 = 0;
    int blue2 = 0;


    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    ColorSensor color;
    ColorSensor color2;

    @Override
    public void runOpMode() {


        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        color = hardwareMap.get(ColorSensor.class, "Color");
        color2 = hardwareMap.get(ColorSensor.class, "Color2");

        telemetry.addData("Path0", "Starting at %7d :%7d");


        waitForStart();
        if (opModeIsActive()) {


           shuffleRight(0.5, 0.5);
           straight(0.5, 0.75);

            while (runtime.seconds() < 1) {
                leftFront.setPower(.5);
                leftBack.setPower(-.5);
                rightFront.setPower(.5);
                rightBack.setPower(-.5);
            }
            runtime.reset();
            motorStop();


            colorSensor(red, green, blue, red2, green2, blue2);
            zeroRings(red, green, blue);
            oneRing(red, green, blue, red2, green2, blue2);
            twoRings(red, green, blue, red2, green2, blue2);
            //encoderDrive(0.8, 0.8, 0.45, 1.635);  // S1: Forward 47 Inches with 5 Sec timeout



            telemetry.addData("Path", "Complete");
            telemetry.update();
        }


    }

    // While the Op Mode is running, update the telemetry values.
    public void colorSensor(int red, int green, int blue, int red2, int green2, int blue2) {
        red = color.red();
        green = color.green();
        blue = color.blue();

        red2 = color2.red();
        green2 = color2.green();
        blue2 = color2.blue();

        telemetry.addData("Red", color.red());
        telemetry.addData("Green", color.green());
        telemetry.addData("Blue", color.blue());
        telemetry.update();

        //orange:
        //red: 1000
        //green: 500
        //blue: 300

        //white:
        //red: 6000
        //green:8000
        //blue: 4500

    }

    public void zeroRings(int red,int green,int blue) {

        if(red <= 900 && green <= 400 && blue <= 200){
            straight(0.5, 1);
            shuffleRight(0.5, 3);
        }
    }

    public void oneRing(int red,int green,int blue, int red2, int green2, int blue2) {

        if((red >= 900 && green >= 400 && blue >= 200) && (red2 <= 900 && green2 <= 400 && blue2 <= 200)){
            straight(0.5, 1);
            shuffleRight(0.5, 4);
            straight(-0.5, 1);
        }
    }

    public void twoRings(int red,int green,int blue, int red2, int green2, int blue2) {

        if((red >= 900 && green >= 400 && blue >= 200)&& (red2 >= 900 && green2 >= 400 && blue2 >= 200)){
            straight(0.5, 1);
            shuffleRight(0.5, 5);
        }
    }
        public void motorStop() {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }

        public void shuffleRight(double speed, double time){
            while (runtime.seconds() < time) {
                leftFront.setPower(speed);
                leftBack.setPower(speed);
                rightFront.setPower(-speed);
                rightBack.setPower(-speed);
            }
            runtime.reset();
            motorStop();
        }

        public void straight(double speed, double time){
            while (runtime.seconds() < time) {
                leftFront.setPower(speed);
                leftBack.setPower(-speed);
                rightFront.setPower(speed);
                rightBack.setPower(-speed);
            }
            runtime.reset();
            motorStop();
        }
    }
  /*


    public void encoderDrive(double speed1, double speed2, double timeoutSh, double timeSt) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // reset the timeout time and start motion.
            runtime.reset();
            //Robot Goes straight for timeSt
            while (opModeIsActive() && (runtime.seconds() < timeSt)) {
                leftFront.setPower(speed2);
                leftBack.setPower(-speed2);
                rightFront.setPower(speed1);
                rightBack.setPower(-speed1);
            }
            //reset runtime
            runtime.reset();
            //Robot Shuffle  for timeoutSh
            while (opModeIsActive() && (runtime.seconds() < timeoutSh)) {

                leftFront.setPower(-speed2);
                leftBack.setPower(-speed2);
                rightFront.setPower(speed1);
                rightBack.setPower(speed1);

                telemetry.addData("Path2", "Running at %.3f ms %.3f", runtime.seconds(), speed1);
                telemetry.update();

            }

            // Stop all motion;
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

        }
    }

     */
