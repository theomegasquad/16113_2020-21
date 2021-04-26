package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Ultimate Goal game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Elmo Autonomous Omega", group = "Concept")
public class ElmoAutonomousOmega extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    private DcMotor intake;
    public DcMotor carousel;
    public DcMotor shooter;
    public DcMotor wobble;
    DigitalChannel digitalTouch;
    DigitalChannel digitalTouch2;
    TouchSensor touch1;
    TouchSensor touch2;
    private Servo claw;

    private ElapsedTime runtime = new ElapsedTime();

    private double speed = 0.7;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AXH/Dlj/////AAABmTBlaEImXk1Cny+eK6viG9EWbNKGi5x4SsfDLYSFFmjPXjxSzph6TlWu81V/lFu/DyFA59a/RPLPqW0cYz6XtBFHRrHKaELfGgEp5ys1ujv24f9CjMMXUqgv7abxcf6qJkaJbEbw4FNox0sJdVyNwVC6WRGsLps0gxFWAqI4kUVL/9F/AjJcutG0JLARlMoJZV7JLXMg7gOlsstWgQPmkgC9AOj/sXWry/T1rU3pHyKzj+XKEvZ+YT8IVhgsLm4gpro7C48fuvotry2AvMFbjN8h2yYIq89d6PteIeYUwyglMgDYI1UuizZhDhufMLM7Y2CoBc8WTq+yEG9QCFiDU/ggJ0/Hs7W8SPNMoTtEQvg7";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        wobble = hardwareMap.dcMotor.get("wobble");
        intake = hardwareMap.dcMotor.get("intake");
        shooter = hardwareMap.dcMotor.get("shooter");
        carousel = hardwareMap.dcMotor.get("carousel");
        claw = hardwareMap.servo.get("claw");

        touch1 = hardwareMap.get(TouchSensor.class, "touch1");
        touch2 = hardwareMap.get(TouchSensor.class, "touch2");

        String label = "Hi";
        int recognitions = 5;




        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        claw.setPosition(0);
        waitForStart();

        if (opModeIsActive()) {

            while (opModeIsActive()) {
                if (!(touch2.isPressed())) {
                    wobble.setPower(0.2);
                    telemetry.addData("Moving", "touch2");
                    telemetry.update();
                }
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        recognitions = updatedRecognitions.size();
                        // step through the list of recognitions and display boundary info.
                        int i = 0;

                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            label = recognition.getLabel();
                            telemetry.addData("Label", label);


                            telemetry.update();
                        }


                        Thread.sleep(3000);



                    }
                }
                if (label == "Quad"){
                    telemetry.addData("Rings:", "4");
                    telemetry.update();
                    encoderDrive(speed, speed,3.5, 2.0);

                    toWhite(0.55 , 1.4, speed);

                    stopMotor();
                    break;
                }
                else if (label == "Single"){
                    telemetry.addData("Rings:", "1");
                    telemetry.update();
                    encoderDrive2(speed, speed,3.0, 2.0, 0.5);

                    leftFront.setPower(0);
                    leftBack.setPower(0);
                    rightFront.setPower(0);
                    rightBack.setPower(0);

                    if (!(touch1.isPressed())) {
                        wobble.setPower(-0.5);
                        telemetry.addData("Moving", "touch2");
                        telemetry.update();
                    }
                    Thread.sleep(1000);
                    claw.setPosition(1);
                    Thread.sleep(1000);
                    if (!(touch2.isPressed())) {
                        wobble.setPower(0.5);
                        telemetry.addData("Moving", "touch2");
                        telemetry.update();
                    }
                    wobble.setPower(0);

                    //hitting wall
                    runtime.reset();
                    while (opModeIsActive() && (runtime.seconds() < 0.6)) {
                        leftFront.setPower(speed);
                        leftBack.setPower(-speed);
                        rightFront.setPower(speed);
                        rightBack.setPower(-speed);
                    }
                    stopMotor();

                    toWhite(0.6, 1.35, speed);
                    break;
                }
                else if (recognitions == 0){
                    telemetry.addData("Rings:", "0");
                    telemetry.update();
                    encoderDrive(speed, speed,1.8, 2.0);

                    leftFront.setPower(0);
                    leftBack.setPower(0);
                    rightFront.setPower(0);
                    rightBack.setPower(0);
                    claw.setPosition(1);
                    Thread.sleep(2000);

                    toWhite2(0.2, 1.4, 0.5,speed);
                    runtime.reset();
                    Thread.sleep(500);
                    break;
                }
            }
        }



        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void encoderDrive(double speed1, double speed2, double timeoutSh, double timeSt) throws InterruptedException {

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


            }

            //turns left
            runtime.reset();
            while (runtime.seconds() < 0.8) {
                leftFront.setPower(0.75);
                leftBack.setPower(0.75);
                rightFront.setPower(0.75);
                rightBack.setPower(0.75);
            }
            stopMotor();
            //hitting wall
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.2)) {

                leftFront.setPower(-speed2);
                leftBack.setPower(-speed2);
                rightFront.setPower(speed1);
                rightBack.setPower(speed1);

            }
            stopMotor();

            //drops goal

            if (!(touch1.isPressed())) {
                wobble.setPower(-0.5);
                telemetry.addData("Moving", "touch2");
                telemetry.update();
            }
            Thread.sleep(1000);
            claw.setPosition(1);
            Thread.sleep(1000);
            if (!(touch2.isPressed())) {
                wobble.setPower(0.5);
                telemetry.addData("Moving", "touch2");
                telemetry.update();
            }
            wobble.setPower(0);

            //shuffle right
            runtime.reset();
            while (runtime.seconds() < 0.6) {
                leftFront.setPower(0.75);
                leftBack.setPower(-0.75);
                rightFront.setPower(0.75);
                rightBack.setPower(-0.75);
            }
            stopMotor();
            //turn right
            runtime.reset();
            while (runtime.seconds() < 0.8) {
                leftFront.setPower(-0.75);
                leftBack.setPower(-0.75);
                rightFront.setPower(-0.75);
                rightBack.setPower(-0.75);
            }
            //hitting wall
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.4)) {
                leftFront.setPower(speed2);
                leftBack.setPower(-speed2);
                rightFront.setPower(speed1);
                rightBack.setPower(-speed1);
            }



            stopMotor();



            //  sleep(250);   // optional pause after each move
        }
    }
    public void encoderDrive2(double speed1, double speed2, double timeoutSh, double timeSt, double timeSs) {

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


            }
            runtime.reset();

            while (opModeIsActive() && (runtime.seconds() < timeSs)) {
                leftFront.setPower(-speed2);
                leftBack.setPower(speed2);
                rightFront.setPower(-speed1);
                rightBack.setPower(speed1);
            }

        }


    }

    public void toWhite (double backTime, double rightTime, double speed) throws InterruptedException {
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < backTime)) {

            leftFront.setPower(speed);
            leftBack.setPower(speed);
            rightFront.setPower(-speed);
            rightBack.setPower(-speed);


        }
        Thread.sleep(500);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < rightTime)) {

            leftFront.setPower(-speed);
            leftBack.setPower(speed);
            rightFront.setPower(-speed);
            rightBack.setPower(speed);


        }
        stopMotor();
        runtime.reset();
        while (runtime.seconds() < 6) {
            shooter.setPower(-0.8);
            Thread.sleep(2000);
            carousel.setPower(1);
            // intake.setPower(-0.6);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {

            leftFront.setPower(-speed);
            leftBack.setPower(-speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);


        }
        stopMotor();
    }

    public void stopMotor(){
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void toWhite2 (double backTime, double rightTime, double frontTime, double speed) throws InterruptedException {


        Thread.sleep(500);
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < rightTime)) {

            leftFront.setPower(-speed);
            leftBack.setPower(speed);
            rightFront.setPower(-speed);
            rightBack.setPower(speed);


        }
        stopMotor();

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < frontTime)) {

            leftFront.setPower(-speed);
            leftBack.setPower(-speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);


        }
        stopMotor();


        runtime.reset();
        while (runtime.seconds() < 6) {
            shooter.setPower(-0.65);
            Thread.sleep(2000);
            carousel.setPower(1);
            // intake.setPower(-0.6);
        }
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {

            leftFront.setPower(-speed);
            leftBack.setPower(-speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);


        }
        stopMotor();
    }

}
