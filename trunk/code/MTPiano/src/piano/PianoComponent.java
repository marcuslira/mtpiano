package piano;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mt4j.components.visibleComponents.AbstractVisibleComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * The PianoComponent is a class that extends AbstractVisibleComponent.
 * This component draws a piano on the screen and plays the piano notes
 * according to the pressed key.
 *
 */
public class PianoComponent extends AbstractVisibleComponent {
	//private PianoGesture[] gestureArray;
	private PApplet app;
	private Plane plane;
	private NotePlayer notePlayer;
	private Set<NotePlayer.Key> activeKeys;
	private Map<Long, Vector3D> activeIds;
		
	public PianoComponent(PApplet applet) {
		super(applet);
		
		notePlayer = new NotePlayer(applet);
		
		this.app = applet;

		this.activeKeys = new HashSet<NotePlayer.Key>();
		this.activeIds = new HashMap<Long, Vector3D>();
		
		this.registerInputProcessor(new MultipleDragProcessor(app));
		this.addGestureListener(MultipleDragProcessor.class, new DragListener());
		
		Vector3D norm = new Vector3D(0,0,1);
		Vector3D pointInPlane = new Vector3D(0,0,0);
		plane = new Plane(pointInPlane, norm);
		
		this.setNoStroke(false);
		this.setStrokeWeight(0.8f);
	}
	
	private void pressKey(NotePlayer.Key k) {
		System.out.println("Key pressed");
		notePlayer.playNote(k);
	}

	private void releaseKey(NotePlayer.Key k) {
		System.out.println("key released");
		notePlayer.stopNote(k);
	}
	
	private synchronized void verifyKeys() {
		// release active keys
		List<NotePlayer.Key> activeKeysToRemove = new LinkedList<NotePlayer.Key>();
		for (NotePlayer.Key k : activeKeys) {
			boolean keyFound = false;
			for (Long id : activeIds.keySet()) {
				Vector3D point = activeIds.get(id);
				NotePlayer.Key auxK = getKey(point.x, point.y);
				if (auxK != null && auxK != NotePlayer.Key.KEY_TYPE_INVALID) {
					keyFound = true;
					break;
				}
			}
			if (!keyFound) {
				activeKeysToRemove.add(k);
			}
		}
		for (NotePlayer.Key k : activeKeysToRemove) {
			activeKeys.remove(k);
			releaseKey(k);
		}
		
		// press new keys
		for (Long id : activeIds.keySet()) {
			Vector3D point = activeIds.get(id);
			NotePlayer.Key k = getKey(point.x, point.y);
			if (k != null && k != NotePlayer.Key.KEY_TYPE_INVALID) {
				if (!activeKeys.contains(k)) {
					activeKeys.add(k);
					pressKey(k);
				}
			}
		}
	}
	
	private NotePlayer.Key getKey(float x, float y) {
		// TODO Auto-generated method stub
		if (x<80 && y<80) {
			return NotePlayer.Key.KEY_TYPE_C;
		} else if (x>80 && x<160 && y<80) {
			return NotePlayer.Key.KEY_TYPE_A;
		} else {
			return NotePlayer.Key.KEY_TYPE_INVALID;
		}
	}
	
	private class DragListener implements IGestureEventListener{
		public boolean processGestureEvent(MTGestureEvent ge) {
			DragEvent de = (DragEvent)ge;
			Vector3D to = de.getTo();
			switch (de.getId()) {
			case DragEvent.GESTURE_DETECTED:{
				//System.out.println("DETECTED X: "+to.x+" || Y: "+to.y+" || ID: "+de.getDragCursor().getId());
				activeIds.put(de.getDragCursor().getId(), to);
			}break;
			case DragEvent.GESTURE_UPDATED:{
				//System.out.println("UPDATED X: "+to.x+" || Y: "+to.y+" || ID: "+de.getDragCursor().getId());
				activeIds.put(de.getDragCursor().getId(), to);
			}break;
			case DragEvent.GESTURE_ENDED:{
				//System.out.println("ENDED   X: "+to.x+" || Y: "+to.y+" || ID: "+de.getDragCursor().getId());
				activeIds.remove(de.getDragCursor().getId());
			}break;
			default:
				break;
			}
			verifyKeys();
			return true;
		}
	}

	@Override
	public void drawComponent(PGraphics g) {
		MTColor strokeCol2 = new MTColor(255, 0, 0, 255);
		g.strokeWeight(this.getStrokeWeight());
		g.stroke(strokeCol2.getR(), strokeCol2.getG(), strokeCol2.getB(), strokeCol2.getAlpha());
		
		MTColor fillCol2 = new MTColor(255, 0, 0, 255);
		g.fill(fillCol2.getR(), fillCol2.getG(), fillCol2.getB(), fillCol2.getAlpha());
		
		
		g.beginShape(PApplet.QUADS);
		g.rect(0, 0, 80, 80);
		g.endShape();
		
		//

		MTColor strokeCol = new MTColor(0, 255, 0, 255);
		g.strokeWeight(this.getStrokeWeight());
		g.stroke(strokeCol.getR(), strokeCol.getG(), strokeCol.getB(), strokeCol.getAlpha());
		
		MTColor fillCol = new MTColor(0, 255, 0, 255);
		g.fill(fillCol.getR(), fillCol.getG(), fillCol.getB(), fillCol.getAlpha());
		
		
		g.beginShape(PApplet.QUADS);
		g.rect(80, 0, 80, 80);
		g.endShape();
	}

	@Override
	protected boolean componentContainsPointLocal(Vector3D testPoint) {
		return plane.componentContainsPointLocal(testPoint);
	}

	@Override
	public Vector3D getIntersectionLocal(Ray ray) {
		return plane.getIntersectionLocal(ray);
	}
	
}
