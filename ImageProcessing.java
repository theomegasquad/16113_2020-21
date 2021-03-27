/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

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
@Autonomous(name = "Concept: TensorFlow Object Detection", group = "Concept")
public class ConceptTensorFlowObjectDetection extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private ElapsedTime runtime = new ElapsedTime();

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
            "AaMp8P3/////AAABmUp+cop5kkZcoogVsra7MsN3NfQLwcjQVMlrZtIuBt2E5WdIRwsD+bWll6ZH5H3uOgXeJZbosG+7amymWJ2a7kPXogaXRlvwD16aLToilo1FTnBkIZh8iTeF7dWZZD9ROfgZj1tPXTqTsYqPkZxaJ+0aMsKPCNlHPBgNWOtc0TBHWR9Fpj1aTVn805p+ENS58CyDW3Xhe9Yb+36OIZzJVnbU1Uy6gs5Bsv6qYm7g09H1jkbRxfJrtdvhoMKX/EK7jcd+kLQw64coW9snQSeNveIdJpx0/62yhOhpkiybhjnn+Ha/ItU7k1cCOtBilRgYvu4nF9zMLRA8qzHjl75acQJpp7SexmW6vDcSgW2lCQic";

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
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

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
            // (typically 16/9).
            tfod.setZoom(1, 10);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            //encoderDrive2(0.8, 0.8,0.2, 0.0);  // S1: Forward 47 Inches with 5 Sec timeout
            //leftFront.setPower(0);
            //leftBack.setPower(0);
            //rightFront.setPower(0);
            //rightBack.setPower(0);
        }

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null)
                    {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i= 0;
                        float height = 0.0f;
                      // step through the list of recognitions and display boundary info.
                        for (Recognition recognition : updatedRecognitions) {
                            i ++;
                            telemetry.addData(String.format("label "), recognition.getLabel());
                            telemetry.addData(String.format("  left,top "), "%.03f , %.03f",
                                    recognition.getWidth(), recognition.getHeight());
                            height= recognition.getHeight();
                        }

                      telemetry.update();
                        runtime.reset();
                        //Robot Shuffle  for timeoutSh
                        while (opModeIsActive() && (runtime.seconds() < 5)) {
                            telemetry.addData(String.format("label ") , "%.3f", height);
                            telemetry.update();
                        }

                        if (i == 0) {
                            encoderDrive(0.8, 0.8,0.45, 1.5);  // S1: Forward 47 Inches with 5 Sec timeout

                            leftFront.setPower(0);
                            leftBack.setPower(0);
                            rightFront.setPower(0);
                            rightBack.setPower(0);
                            break;
                        }
                        else if (i == 1 && height < 140) {
                            encoderDrive2(0.8, 0.8,0.45, 2.0);  // S1: Forward 47 Inches with 5 Sec timeout

                            leftFront.setPower(0);
                            leftBack.setPower(0);
                            rightFront.setPower(0);
                            rightBack.setPower(0);
                            break;
                        }
                        else if (i == 1 && height > 140){
                            encoderDrive3(0.8, 0.8,0.38, 2.4);  // S1: Forward 47 Inches with 5 Sec timeout
                            leftFront.setPower(0);
                            leftBack.setPower(0);
                            rightFront.setPower(0);
                            rightBack.setPower(0);
                            break;

                        }
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }
    public void encoderDrive4(double speed1, double speed2, double timeoutSh, double timeSt) {
        while (opModeIsActive() && (runtime.seconds() < timeoutSh)) {

            leftFront.setPower(speed2);
            leftBack.setPower(speed2);
            rightFront.setPower(-speed1);
            rightBack.setPower(-speed1);
        }
        while (opModeIsActive() && (runtime.seconds() < timeSt)) {
            leftFront.setPower(speed2);
            leftBack.setPower(-speed2);
            rightFront.setPower(speed1);
            rightBack.setPower(-speed1);
        }
    }// S1: Forward 47 Inches with 5 Sec timeout

    public void encoderDrive3(double speed1, double speed2, double timeoutSh, double timeSt) {

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

                leftFront.setPower(speed2);
                leftBack.setPower(speed2);
                rightFront.setPower(-speed1);
                rightBack.setPower(-speed1);

                telemetry.addData("Path2", "Running at %.3f ms %.3f", runtime.seconds(), speed1);
                telemetry.update();

            }


            //  sleep(250);   // optional pause after each move
        }
    }

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

                leftFront.setPower(speed2);
                leftBack.setPower(speed2);
                rightFront.setPower(-speed1);
                rightBack.setPower(-speed1);

                telemetry.addData("Path2", "Running at %.3f ms %.3f", runtime.seconds(), speed1);
                telemetry.update();

            }


            //  sleep(250);   // optional pause after each move
        }
    }
    public void encoderDrive2(double speed1, double speed2, double timeoutSh, double timeSt) {

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
        parameters.cameraDirection = CameraDirection.BACK;

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
}
