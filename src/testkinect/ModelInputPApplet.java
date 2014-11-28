package testkinect;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class ModelInputPApplet extends PApplet {
	SimpleOpenNI context;
	SkeletonOrientations skeleton = new SkeletonOrientations();
	long time = System.currentTimeMillis();
	ArrayList<String> inputData = new ArrayList<String>();
	
	public void setup() {
		
		context = new SimpleOpenNI(this);
		System.out.println("Context test...");

		// enable depthMap generation
		context.enableDepth();
		
		context.setMirror(true);
		
		context.enableRGB();

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		
		size(context.depthWidth(), context.depthHeight());
	}

	public void draw() {
		context.update();
//		System.out.println("context: " + context);
		if (context == null || context.depthImage() == null) {
			return;
		}
		image(context.depthImage(),0,0);

		int i = 0;
		
		for (i = 0; i <= 10; i++) {
			// check if the skeleton is being tracked
			if (context.isTrackingSkeleton(i)) {
				drawSkeleton(i);
				if (System.currentTimeMillis() - time >= 100) {
					System.out.println("Saving model!!!");
					this.skeleton.saveSekeleton(context);
					System.out.print(skeleton.toString());
					inputData.add(this.skeleton.toDataEntry());
					this.time = System.currentTimeMillis();
				}
			}
		}
		
	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId) {
		stroke(0, 204, 102);
		strokeWeight(6);
		
		// draw limbs
		context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT); 
		

	}

	public void onNewUser(int userId) {
		System.out.println("New User Detected - userId: " + userId);

		// start pose detection
		context.startPoseDetection("Psi", userId);
	}

	public void onLostUser(int userId) {
		System.out.println("User Lost - userId: " + userId);
	}

	public void onStartPose(String pose, int userId) {
		System.out.println("Start of Pose Detected  - userId: " + userId
				+ ", pose: " + pose);

		// stop pose detection
		context.stopPoseDetection(userId);

		// start attempting to calibrate the skeleton
		context.requestCalibrationSkeleton(userId, true);
	}

	// when calibration begins
	public void onStartCalibration(int userId) {
		System.out.println("Beginning Calibration - userId: " + userId);
	}

	// when calibaration ends - successfully or unsucessfully
	public void onEndCalibration(int userId, boolean successfull) {
		System.out.println("Calibration of userId: " + userId
				+ ", successfull: " + successfull);

		if (successfull) {
			System.out.println("  User calibrated !!!");
			// begin skeleton tracking
			context.startTrackingSkeleton(userId);
		} else {
			System.out.println("  Failed to calibrate user !!!");

			// Start pose detection
			context.startPoseDetection("Psi", userId);
		}
	}
	
	@Override
	public void destroy() {
		//write skeleton to file.
		try {
			BufferedWriter wt = new BufferedWriter(new FileWriter("inputData"));
			for(int i = 0; i < inputData.size(); i++) {
				wt.write(inputData.get(i));
				wt.flush();
			}
			wt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		context.close();
		super.destroy();
	}
}
