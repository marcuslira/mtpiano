package piano;

import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

/**
 * Scene to use the PianoComponent.
 *
 */
public class PianoScene extends AbstractScene {
	private MTApplication mtApp;
	private PianoComponent piano;
	
	public PianoScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.mtApp = mtApplication;
		this.setClearColor(new MTColor(0, 0, 0, 255));
		
		//Create tail component
		piano = new PianoComponent(mtApp);
		this.getCanvas().addChild(piano);
		
		//Add tap&hold gesture to clear all tails
		TapAndHoldProcessor tapAndHold = new TapAndHoldProcessor(mtApplication);
		tapAndHold.setMaxFingerUpDist(10);
		tapAndHold.setHoldTime(3000);
		piano.registerInputProcessor(tapAndHold);
		piano.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent t = (TapAndHoldEvent)ge;
				if (t.getId() == TapAndHoldEvent.GESTURE_ENDED && t.isHoldComplete()){
					//
				}
				return false;
			}
		});
		piano.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(mtApp, getCanvas()));
		
		//Add touch feedback
		this.registerGlobalInputProcessor(new CursorTracer(mtApp, this));
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

}
