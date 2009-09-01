package org.pathwayeditor.notations.sbgner.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LabelLocationPolicy;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LineStyle;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LinkEndDecoratorShape;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.RGB;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPlainTextPropertyDefinition;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPropertyDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSyntaxService;
import org.pathwayeditor.businessobjects.typedefn.ILinkObjectType;
import org.pathwayeditor.businessobjects.typedefn.IObjectType;
import org.pathwayeditor.businessobjects.typedefn.IRootObjectType;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType;
import org.pathwayeditor.businessobjects.typedefn.ILinkObjectType.LinkEditableAttributes;
import org.pathwayeditor.businessobjects.typedefn.ILinkTerminusDefinition.LinkTermEditableAttributes;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType.EditableShapeAttributes;
import org.pathwayeditor.figure.geometry.Dimension;
import org.pathwayeditor.notationsubsystem.toolkit.definition.IntegerPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.LinkObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.definition.LinkTerminusDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.NumberPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.PlainTextPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.RootObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.definition.ShapeObjectType;

public class SbgnERNotationSyntaxService implements INotationSyntaxService {
	private static final int NUM_ROOT_OTS = 1;
	private static final char TAU = 964;
	private static final String UNIT_OF_INFO_DEFN = "(C) setanchor\n"
		+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "x y w h rect";
	private static final String STATE_DEFN = "(C) setanchor\n"
		+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "h w le  {x y w h 1 h mul 1 h mul rrect}\n"
			+ "{x y w h 1 w mul 1 w mul rrect} ifelse";
	private static final String EXISTENCE_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval \n" 
			+ "gsave\n"
			+ "curlinecol setfillcol\n"
			+ "x y w h -90 180 arc grestore";
	private static final String LOCATION_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval\n "
			+ "/rf {45 cos 1 add 0.5 mul} def /lf {45 sin 3 div neg 1 add 0.5 mul} def \n"
			+ "w lf mul x add h lf mul y add w rf mul x add h rf mul y add line\n"
			+ "64.5 cos 1.0 add 2.0 div w mul x add \n"
			+ "1.0 64.5 sin sub 2.0 div h mul y add \n"
			+ "1.0 25.5 cos sub 2.0 div w mul x add \n"
			+ "25.5 sin 1.0 add 2.0 div h mul y add \n" + "line";
	private static final String OBSERVABLE_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "[0.25 xoffset 0 yoffset 0.75 xoffset 0 yoffset 1.00 xoffset 0.50 yoffset 0.75 xoffset 1.00 yoffset 0.25 xoffset 1.00 yoffset 0 xoffset 0.50 yoffset] pgon";
	private static final String PERTURBATION_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "[0 xoffset 0 yoffset 1.00 xoffset 0 yoffset 0.70 xoffset 0.50 yoffset 1.00 xoffset 1.00 yoffset 0 xoffset 1.00 yoffset 0.30 xoffset 0.50 yoffset] pgon";
	private static final String ENTITY_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ " x y w h 0.2 w mul 0.20 h mul rrect ";
	private static final String OUTCOME_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "gsave curlinecol setfillcol\n" + "x y w h oval grestore";
	private static final String INTERACT_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "x y w h oval ";
	private static final String AND_SHAPE_DEFN = "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval h 0.35 mul setfontsize null setfillcol 0.5 xoffset 0.5 yoffset (C) (AND) text\n"
			+ "0 xoffset 0.50 yoffset -0.20 xoffset 0.50 yoffset line\n"
			+ "1.20 xoffset 0.50 yoffset 1.00 xoffset 0.50 yoffset line\n"
			+ "[-0.2 xoffset 0.5 yoffset 1.2 xoffset 0.5 yoffset ] (S) setanchor\n";
	private static final String NOT_SHAPE_DEFN = "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval h 0.35 mul setfontsize null setfillcol 0.5 xoffset 0.5 yoffset (C) (NOT) text\n"
			+ "0 xoffset 0.50 yoffset -0.20 xoffset 0.50 yoffset line\n"
			+ "1.20 xoffset 0.50 yoffset 1.00 xoffset 0.50 yoffset line\n"
			+ "[-0.2 xoffset 0.5 yoffset 1.2 xoffset 0.5 yoffset ] (S) setanchor\n";
	private static final String OR_SHAPE_DEFN = "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval h 0.35 mul setfontsize null setfillcol 0.5 xoffset 0.5 yoffset (C) (OR) text\n"
			+ "0 xoffset 0.50 yoffset -0.20 xoffset 0.50 yoffset line\n"
			+ "1.20 xoffset 0.50 yoffset 1.00 xoffset 0.50 yoffset line\n"
			+ "[-0.2 xoffset 0.5 yoffset 1.2 xoffset 0.5 yoffset ] (S) setanchor\n";
	private static final String DELAY_SHAPE_DEFN = "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "x y w h oval h 0.35 mul setfontsize null setfillcol 0.5 xoffset 0.5 yoffset (C) ("
			+ TAU
			+ ") text\n"
			+ "0 xoffset 0.50 yoffset -0.20 xoffset 0.50 yoffset line\n"
			+ "1.20 xoffset 0.50 yoffset 1.00 xoffset 0.50 yoffset line\n"
			+ "[-0.2 xoffset 0.5 yoffset 1.2 xoffset 0.5 yoffset ] (S) setanchor\n";
	private static final String NOTE_DEFN = "(C) setanchor\n"
			+ "curbounds /h exch def /w exch def /y exch def /x exch def\n"
			+ "/xoffset { w mul x add } def /yoffset { h mul y add } def\n"
			+ "[0 xoffset 0 yoffset 0.8 xoffset 0 yoffset 1 xoffset 0.2 yoffset 1.00 xoffset 1.00 yoffset 0 xoffset 1.00 yoffset] pgon\n"
			+ "gsave curlinecol setfillcol\n"
			+ "[0.8 xoffset 0 yoffset 0.8 xoffset 0.2 yoffset 1 xoffset 0.2 yoffset] pgon grestore";

	private final INotation context;
	private final Map<Integer, IShapeObjectType> shapes = new HashMap<Integer, IShapeObjectType>();
	private final Map<Integer, ILinkObjectType> links = new HashMap<Integer, ILinkObjectType>();

	private RootObjectType rmo;
	// shapes
	private ShapeObjectType State;
	private ShapeObjectType LocState;
	private ShapeObjectType ExState;
	private ShapeObjectType UnitOfInf;
	private ShapeObjectType Entity;
	private ShapeObjectType Interaction;
	private ShapeObjectType Outcome;
	private ShapeObjectType Perturbation;
	private ShapeObjectType Observable;
	private ShapeObjectType AndGate;
	private ShapeObjectType OrGate;
	private ShapeObjectType NotGate;
	private ShapeObjectType DelayGate;
	private ShapeObjectType Decision;
	private ShapeObjectType Acceptor;
	private ShapeObjectType Note;

	// links
	private LinkObjectType Modulation;
	private LinkObjectType Stimulation;
	private LinkObjectType Inhibition;
	private LinkObjectType AbsInhibition;
	private LinkObjectType NecessaryStimulation;
	private LinkObjectType AbsStimulation;
	private LinkObjectType LogicArc;
	private LinkObjectType InteractionArc;
	private LinkObjectType AssignmenArc;
	private LinkObjectType VarArc;
	private LinkObjectType AnnotationArc;

	private INotationSubsystem serviceProvider;

	public SbgnERNotationSyntaxService(INotationSubsystem serviceProvider) {
		this.serviceProvider = serviceProvider;
		this.context = serviceProvider.getNotation();
		// "SBGN-PD"
		// "SBGN process diagram language context"
		// 1_0_0
		createRMO();
		// shapes
		this.State = new ShapeObjectType(this, 10, "State");
		createState();
		this.LocState = new ShapeObjectType(this, 11, "LocState");
		createLocState();
		this.ExState = new ShapeObjectType(this, 12, "ExState");
		createExState();
		this.UnitOfInf = new ShapeObjectType(this, 13, "Unit Of Information");
		createUnitOfInf();
		this.Entity = new ShapeObjectType(this, 14, "Entity");
		createEntity();
		this.Interaction = new ShapeObjectType(this, 15, "Interaction");
		createInteraction();
		this.Outcome = new ShapeObjectType(this, 16, "Outcome");
		createOutcome();
		this.Perturbation = new ShapeObjectType(this, 113, "Perturbing Agent");
		createPerturbation();
		this.Observable = new ShapeObjectType(this, 114, "Observable");
		createObservable();
		this.AndGate = new ShapeObjectType(this, 123, "AND");
		createAndGate();
		this.OrGate = new ShapeObjectType(this, 124, "OR");
		createOrGate();
		this.NotGate = new ShapeObjectType(this, 125, "NOT");
		createNotGate();
		this.Decision = new ShapeObjectType(this, 116, "Decision");
		createDecision();
		this.Acceptor = new ShapeObjectType(this, 117, "Acceptor");
		createAcceptor();
		this.DelayGate = new ShapeObjectType(this, 115, "DelayGate");
		createDelayGate();
		this.Note = new ShapeObjectType(this, 118, "Note");
		createNote();

		defineParentingRMO();
		// shapes parenting
		defineParentingState();
		defineParentingLocState();
		defineParentingExState();
		defineParentingUnitOfInf();
		defineParentingEntity();
		defineParentingInteraction();
		defineParentingOutcome();
		defineParentingPerturbation();
		defineParentingObservable();
		defineParentingNote();
		defineParentingAndGate();
		defineParentingOrGate();
		defineParentingNotGate();
		defineParentingDelayGate();
		defineParentingDecision();
		defineParentingAcceptor();

		// links
		this.Modulation = new LinkObjectType(this, 20, "Modulation");
		createModulation();
		this.Stimulation = new LinkObjectType(this, 21, "Stimulation");
		createStimulation();
		this.Inhibition = new LinkObjectType(this, 23, "Inhibition");
		createInhibition();
		this.AbsInhibition = new LinkObjectType(this, 24, "AbsInhibition");
		createAbsInhibition();
		this.NecessaryStimulation = new LinkObjectType(this, 25,
				"NecessaryStimulation");
		createNecessaryStimulation();
		this.AbsStimulation = new LinkObjectType(this, 221, "AbsStimulation");
		createAbsStimulation();
		this.LogicArc = new LinkObjectType(this, 26, "LogicArc");
		createLogicArc();
		this.InteractionArc = new LinkObjectType(this, 27, "InteractionArc");
		createInteractionArc();
		this.AssignmenArc = new LinkObjectType(this, 28, "AssignmenArc");
		createAssignmenArc();
		this.VarArc = new LinkObjectType(this, 29, "VarArc");
		createVarArc();
		this.AnnotationArc = new LinkObjectType(this, 210, "AnnotationArc");
		createAnnotationArc();

		this.shapes.put(this.State.getUniqueId(), this.State);
		this.shapes.put(this.LocState.getUniqueId(), this.LocState);
		this.shapes.put(this.ExState.getUniqueId(), this.ExState);
		this.shapes.put(this.UnitOfInf.getUniqueId(), this.UnitOfInf);
		this.shapes.put(this.Entity.getUniqueId(), this.Entity);
		this.shapes.put(this.Interaction.getUniqueId(), this.Interaction);
		this.shapes.put(this.Outcome.getUniqueId(), this.Outcome);
		this.shapes.put(this.Perturbation.getUniqueId(), this.Perturbation);
		this.shapes.put(this.Observable.getUniqueId(), this.Observable);
		this.shapes.put(this.Note.getUniqueId(), this.Note);
		this.shapes.put(this.AndGate.getUniqueId(), this.AndGate);
		this.shapes.put(this.OrGate.getUniqueId(), this.OrGate);
		this.shapes.put(this.NotGate.getUniqueId(), this.NotGate);
		this.shapes.put(this.DelayGate.getUniqueId(), this.DelayGate);
		this.shapes.put(this.Decision.getUniqueId(), this.Decision);
		this.shapes.put(this.Acceptor.getUniqueId(), this.Acceptor);

		// link set
		this.links.put(this.Modulation.getUniqueId(), this.Modulation);
		this.links.put(this.Stimulation.getUniqueId(), this.Stimulation);
		this.links.put(this.Inhibition.getUniqueId(), this.Inhibition);
		this.links.put(this.AbsInhibition.getUniqueId(), this.AbsInhibition);
		this.links.put(this.NecessaryStimulation.getUniqueId(),
				this.NecessaryStimulation);
		this.links.put(this.AbsStimulation.getUniqueId(), this.AbsStimulation);
		this.links.put(this.LogicArc.getUniqueId(), this.LogicArc);
		this.links.put(this.InteractionArc.getUniqueId(), this.InteractionArc);
		this.links.put(this.AssignmenArc.getUniqueId(), this.AssignmenArc);
		this.links.put(this.VarArc.getUniqueId(), this.VarArc);
		this.links.put(this.AnnotationArc.getUniqueId(), this.AnnotationArc);
	}

	public INotationSubsystem getNotationSubsystem() {
		return serviceProvider;
	}

	public INotation getNotation() {
		return this.context;
	}

	public Iterator<ILinkObjectType> linkTypeIterator() {
		return this.links.values().iterator();
	}

	public IRootObjectType getRootObjectType() {
		return this.rmo;
	}

	public Iterator<IShapeObjectType> shapeTypeIterator() {
		return this.shapes.values().iterator();
	}

	public Iterator<IObjectType> objectTypeIterator() {
		Set<IObjectType> retVal = new HashSet<IObjectType>(this.shapes.values());
		retVal.addAll(this.links.values());
		retVal.add(this.rmo);
		return retVal.iterator();
	}

	public boolean containsLinkObjectType(int uniqueId) {
		return this.links.containsKey(uniqueId);
	}

	public boolean containsObjectType(int uniqueId) {
		boolean retVal = this.links.containsKey(uniqueId);
		if (!retVal) {
			retVal = this.shapes.containsKey(uniqueId);
		}
		if (!retVal) {
			retVal = this.rmo.getUniqueId() == uniqueId;
		}
		return retVal;
	}

	public boolean containsShapeObjectType(int uniqueId) {
		return this.shapes.containsKey(uniqueId);
	}

	public ILinkObjectType getLinkObjectType(int uniqueId) {
		return this.links.get(uniqueId);
	}

	public IObjectType getObjectType(int uniqueId) {
		IObjectType retVal = this.links.get(uniqueId);
		if (retVal == null) {
			retVal = this.shapes.get(uniqueId);
		}
		if (retVal == null) {
			retVal = (this.rmo.getUniqueId() == uniqueId) ? this.rmo : null;
		}
		if (retVal == null) {
			throw new IllegalArgumentException(
					"The unique Id was not present in this notation subsystem");
		}
		return retVal;
	}

	public IShapeObjectType getShapeObjectType(int uniqueId) {
		return this.shapes.get(uniqueId);
	}

	private <T extends IObjectType> T findObjectTypeByName(
			Collection<? extends T> otSet, String name) {
		T retVal = null;
		for (T val : otSet) {
			if (val.getName().equals(name)) {
				retVal = val;
				break;
			}
		}
		return retVal;
	}

	public ILinkObjectType findLinkObjectTypeByName(String name) {
		return findObjectTypeByName(this.links.values(), name);
	}

	public IShapeObjectType findShapeObjectTypeByName(String name) {
		return findObjectTypeByName(this.shapes.values(), name);
	}

	public int numLinkObjectTypes() {
		return this.links.size();
	}

	public int numShapeObjectTypes() {
		return this.shapes.size();
	}

	public int numObjectTypes() {
		return this.numLinkObjectTypes() + this.numShapeObjectTypes()
				+ NUM_ROOT_OTS;
	}

	private void createRMO() {
		this.rmo = new RootObjectType(0, "Root Object", "ROOT_OBJECT", this);
	}

	private void defineParentingRMO() {
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.State,
				this.LocState, this.ExState, this.UnitOfInf, this.Entity,
				this.Interaction, this.Outcome, this.Perturbation,
				this.Observable, this.AndGate, this.OrGate, this.NotGate,
				this.DelayGate, this.Decision, this.Acceptor, this.Note }));
		set.removeAll(Arrays.asList(new IShapeObjectType[] { this.LocState,
				this.UnitOfInf, this.ExState }));
		for (IShapeObjectType child : set) {
			this.rmo.getParentingRules().addChild(child);
		}

	}

	private void createState() {
		this.State.setDescription("State variable value");// ment to be
		// TypeDescription
		// rather
		PlainTextPropertyDefinition stateDescnProp = new PlainTextPropertyDefinition(
				"stateValue", "?");
		stateDescnProp.setAlwaysDisplayed(true);
		stateDescnProp.setEditable(true);
		stateDescnProp.setDisplayName("Value");
		stateDescnProp.getLabelDefaults().setNoBorder(true);
		stateDescnProp.getLabelDefaults().setNoFill(true);
		stateDescnProp.getLabelDefaults().setMinimumSize(new Dimension(30, 30));
		this.State.getDefaultAttributes().addPropertyDefinition(stateDescnProp);
		this.State.getDefaultAttributes().setShapeDefinition(STATE_DEFN);
		this.State.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.State.getDefaultAttributes().setLineWidth(1);
		this.State.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.State.getDefaultAttributes().setLineColour(RGB.BLACK);
		this.State.getDefaultAttributes().setSize(new Dimension(40, 30));

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.State.setEditableAttributes(editableAttributes);
	}

	private void defineParentingState() {
		this.State.getParentingRules().clear();
	}

	public ShapeObjectType getState() {
		return this.State;
	}

	private void createLocState() {
		this.LocState.setDescription("Location State variable");// ment to be
		// TypeDescription
		// rather
		// this.LocState.getDefaultAttributes().setName("LocationState");
		this.LocState.getDefaultAttributes().setShapeDefinition(LOCATION_DEFN);
		this.LocState.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.LocState.getDefaultAttributes().setSize(new Dimension(20, 20));
		this.LocState.getDefaultAttributes().setLineWidth(2);
		this.LocState.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.LocState.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		// this.LocState.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.LocState.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.LocState.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.LocState.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.LocState.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.LocState.getDefaultAttributes().setLineColourEditable(true);
		this.LocState.setEditableAttributes(editableAttributes);

	}

	private void defineParentingLocState() {
		this.LocState.getParentingRules().clear();
	}

	public ShapeObjectType getLocState() {
		return this.LocState;
	}

	private void createExState() {
		this.ExState.setDescription("Existence State variable");// ment to be
		// TypeDescription
		// rather
		// this.ExState.getDefaultAttributes().setName("ExistenceState");
		this.ExState.getDefaultAttributes().setShapeDefinition(EXISTENCE_DEFN);
		;
		this.ExState.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.ExState.getDefaultAttributes().setSize(new Dimension(20, 20));
		this.ExState.getDefaultAttributes().setLineWidth(1);
		this.ExState.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.ExState.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		// this.ExState.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.ExState.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.ExState.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.ExState.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.ExState.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.ExState.getDefaultAttributes().setLineColourEditable(true);
		this.ExState.setEditableAttributes(editableAttributes);
	}

	private void defineParentingExState() {
		this.ExState.getParentingRules().clear();
	}

	public ShapeObjectType getExState() {
		return this.ExState;
	}

	private void createUnitOfInf() {
		this.UnitOfInf.setDescription("Auxiliary information box");// ment to be
		// TypeDescription
		// rather
		PlainTextPropertyDefinition infoDescnProp = new PlainTextPropertyDefinition(
				"unitOfInfo", "Info");
		infoDescnProp.setAlwaysDisplayed(true);
		infoDescnProp.setEditable(true);
		infoDescnProp.setDisplayName("Information");
		this.UnitOfInf.getDefaultAttributes().addPropertyDefinition(
				infoDescnProp);
		this.UnitOfInf.getDefaultAttributes().setShapeDefinition(
				UNIT_OF_INFO_DEFN);
		this.UnitOfInf.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.UnitOfInf.getDefaultAttributes().setSize(new Dimension(45, 25));
		this.UnitOfInf.getDefaultAttributes().setLineWidth(1);
		this.UnitOfInf.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.UnitOfInf.getDefaultAttributes().setLineColour(RGB.BLACK);
		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.UnitOfInf.setEditableAttributes(editableAttributes);
	}

	private void defineParentingUnitOfInf() {
		this.UnitOfInf.getParentingRules().clear();
	}

	public ShapeObjectType getUnitOfInf() {
		return this.UnitOfInf;
	}

	private NumberPropertyDefinition createStoichFontSizeProperty() {
		NumberPropertyDefinition cardFontSizeProp = new NumberPropertyDefinition(
				"stoichFontSize", new BigDecimal(12.0));
		cardFontSizeProp.setVisualisable(false);
		cardFontSizeProp.setEditable(true);
		cardFontSizeProp.setDisplayName("Stoichiometry Font Size");
		return cardFontSizeProp;
	}

	private IntegerPropertyDefinition createStoichiometryProperty() {
		IntegerPropertyDefinition stoichProp = new IntegerPropertyDefinition(
				"stoichiometry", 1);
		stoichProp.setVisualisable(true);
		stoichProp.setEditable(true);
		stoichProp.setDisplayName("Stoichiometry");
		stoichProp.getLabelDefaults().setLineWidth(1.0);
		stoichProp.getLabelDefaults().setFillColour(RGB.WHITE);
		stoichProp.getLabelDefaults().setLineColour(RGB.BLACK);
		stoichProp.getLabelDefaults().setNoFill(false);
		stoichProp.getLabelDefaults().setNoBorder(false);
		stoichProp.getLabelDefaults().setLabelLocationPolicy(
				LabelLocationPolicy.COMPASS);
		return stoichProp;
	}

	private IPlainTextPropertyDefinition createNameProperty() {
		PlainTextPropertyDefinition nameProp = new PlainTextPropertyDefinition(
				"name", "Name");
		nameProp.setAlwaysDisplayed(true);
		nameProp.setEditable(true);
		nameProp.setDisplayName("Name");
		nameProp.getLabelDefaults().setNoFill(true);
		nameProp.getLabelDefaults().setNoBorder(true);
		return nameProp;
	}

	private void createEntity() {
		this.Entity.setDescription("Interacting entity");// ment to be
		// TypeDescription
		// rather
		this.Entity.getDefaultAttributes().addPropertyDefinition(
				createNameProperty());
		this.Entity.getDefaultAttributes().setShapeDefinition(ENTITY_DEFN);
		this.Entity.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Entity.getDefaultAttributes().setSize(new Dimension(60, 40));
		this.Entity.getDefaultAttributes().setLineWidth(1);
		this.Entity.getDefaultAttributes().setLineColour(RGB.BLACK);
		this.Entity.getDefaultAttributes().setLineStyle(LineStyle.SOLID);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		// this.Entity.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.Entity.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.Entity.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.Entity.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.Entity.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.Entity.getDefaultAttributes().setLineColourEditable(true);
		this.Entity.setEditableAttributes(editableAttributes);
	}

	private void defineParentingEntity() {
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.State, this.ExState, this.LocState,
				this.UnitOfInf }));
		for (IShapeObjectType child : set) {
			this.Entity.getParentingRules().addChild(child);
		}

	}

	public ShapeObjectType getEntity() {
		return this.Entity;
	}

	private void createInteraction() {
		this.Interaction.setDescription("Interacting entity");// ment to be
		this.Interaction.getDefaultAttributes().setShapeDefinition(
				INTERACT_DEFN);
		this.Interaction.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Interaction.getDefaultAttributes().setSize(new Dimension(30, 30));
		this.Interaction.getDefaultAttributes().setLineWidth(1);
		this.Interaction.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Interaction.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		// this.Interaction.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.Interaction.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.Interaction.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.Interaction.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.Interaction.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.Interaction.getDefaultAttributes().setLineColourEditable(true);
		this.Interaction.setEditableAttributes(editableAttributes);
	}

	private void defineParentingInteraction() {
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Outcome }));
		for (IShapeObjectType child : set) {
			this.Interaction.getParentingRules().addChild(child);
		}

	}

	public ShapeObjectType getInteraction() {
		return this.Interaction;
	}

	private void createOutcome() {
		this.Outcome.setDescription("Interaction outcome");// ment to be
		// TypeDescription
		// rather
		// this.Outcome.getDefaultAttributes().setName("Outcome");
		this.Outcome.getDefaultAttributes().setShapeDefinition(OUTCOME_DEFN);
		this.Outcome.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Outcome.getDefaultAttributes().setSize(new Dimension(10, 10));
		int[] lc = new int[] { 0, 0, 0 };
		this.Outcome.getDefaultAttributes().setLineWidth(1);
		this.Outcome.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Outcome.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		// this.Outcome.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.Outcome.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.Outcome.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.Outcome.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.Outcome.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.Outcome.getDefaultAttributes().setLineColourEditable(true);
		this.Outcome.setEditableAttributes(editableAttributes);
	}

	private void defineParentingOutcome() {
		this.Outcome.getParentingRules().clear();
	}

	public ShapeObjectType getOutcome() {
		return this.Outcome;
	}

	private void createDecision() {
		this.Decision.setDescription("agregator for several state assignments");// ment
		// to
		// be
		// TypeDescription
		// rather
		// this.Decision.getDefaultAttributes().setName("Decision");
		this.Decision.getDefaultAttributes().setShapeDefinition(OUTCOME_DEFN);
		this.Decision.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Decision.getDefaultAttributes().setSize(new Dimension(4, 4));
		this.Decision.getDefaultAttributes().setLineWidth(1);
		this.Decision.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Decision.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		// this.Decision.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.Decision.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.Decision.setSizeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
		}
		// this.Decision.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.Decision.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.Decision.getDefaultAttributes().setLineColourEditable(true);
		this.Decision.setEditableAttributes(editableAttributes);
	}

	private void defineParentingDecision() {
		this.Decision.getParentingRules().clear();
	}

	public ShapeObjectType getDecision() {
		return this.Decision;
	}

	private void createAcceptor() {
		this.Acceptor.setDescription("acceptor of influence link");// ment to be
		// TypeDescription
		// rather
		// this.Acceptor.getDefaultAttributes().setName("Acceptor");
		this.Acceptor.getDefaultAttributes().setShapeDefinition(OUTCOME_DEFN);
		this.Acceptor.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Acceptor.getDefaultAttributes().setSize(new Dimension(4, 4));
		int[] lc = new int[] { 0, 0, 0 };
		this.Acceptor.getDefaultAttributes().setLineWidth(1);
		this.Acceptor.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Acceptor.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		// this.Acceptor.getDefaultAttributes().setFillEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
		}
		// this.Acceptor.setPrimitiveShapeTypeEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		// this.Acceptor.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		// this.Acceptor.getDefaultAttributes().setLineWidthEditable(true);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		// this.Acceptor.getDefaultAttributes().setLineColourEditable(true);
		this.Acceptor.setEditableAttributes(editableAttributes);
	}

	private void defineParentingAcceptor() {
		this.Acceptor.getParentingRules().clear();
	}

	public ShapeObjectType getAcceptor() {
		return this.Acceptor;
	}

	private void createPerturbation() {
		this.Perturbation.setDescription("Perturbing Agent");// ment to be
		// TypeDescription
		// rather
		this.Perturbation.getDefaultAttributes().addPropertyDefinition(
				createNameProperty());
		this.Perturbation.getDefaultAttributes().setShapeDefinition(
				PERTURBATION_DEFN);
		this.Perturbation.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.Perturbation.getDefaultAttributes().setSize(new Dimension(80, 60));
		this.Perturbation.getDefaultAttributes().setLineWidth(1);
		this.Perturbation.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Perturbation.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.Perturbation.setEditableAttributes(editableAttributes);
	}

	private void defineParentingPerturbation() {
		this.Perturbation.getParentingRules().clear();
	}

	public ShapeObjectType getPerturbation() {
		return this.Perturbation;
	}

	private void createObservable() {
		this.Observable.setDescription("Observable");// ment to be
		// TypeDescription
		// rather
		this.Observable.getDefaultAttributes().setShapeDefinition(
				OBSERVABLE_DEFN);
		this.Observable.getDefaultAttributes().addPropertyDefinition(
				createNameProperty());
		this.Observable.getDefaultAttributes().setFillColour(
				new RGB(255, 255, 255));
		this.Observable.getDefaultAttributes().setLineColour(RGB.BLACK);
		this.Observable.getDefaultAttributes().setSize(new Dimension(80, 60));
		this.Observable.getDefaultAttributes().setLineWidth(1);
		this.Observable.getDefaultAttributes().setLineStyle(LineStyle.SOLID);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.Observable.setEditableAttributes(editableAttributes);
	}

	private void defineParentingObservable() {
		this.Observable.getParentingRules().clear();
	}

	public ShapeObjectType getObservable() {
		return this.Observable;
	}

	private void createNote() {
		this.Note.setDescription("Annotation");// ment to be
		// TypeDescription
		// rather
		this.Note.getDefaultAttributes().setShapeDefinition(NOTE_DEFN);
		this.Note.getDefaultAttributes().addPropertyDefinition(
				createNameProperty());
		this.Note.getDefaultAttributes().setFillColour(new RGB(255, 255, 255));
		this.Note.getDefaultAttributes().setLineColour(RGB.BLACK);
		this.Note.getDefaultAttributes().setSize(new Dimension(80, 60));
		this.Note.getDefaultAttributes().setLineWidth(1);
		this.Note.getDefaultAttributes().setLineStyle(LineStyle.SOLID);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.Note.setEditableAttributes(editableAttributes);
	}

	private void defineParentingNote() {
		this.Note.getParentingRules().clear();
	}

	public ShapeObjectType getNote() {
		return this.Note;
	}

	private void createDelayGate() {
		this.DelayGate.setDescription("DelayGate");// ment to be TypeDescription
		// rather
		this.DelayGate.getDefaultAttributes().setShapeDefinition(
				DELAY_SHAPE_DEFN);
		this.DelayGate.getDefaultAttributes().setFillColour(
				new RGB(255, 255, 255));
		this.DelayGate.getDefaultAttributes().setSize(new Dimension(30, 30));
		this.DelayGate.getDefaultAttributes().setLineWidth(1);
		this.DelayGate.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.DelayGate.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.DelayGate.setEditableAttributes(editableAttributes);
	}

	private void defineParentingDelayGate() {
		this.DelayGate.getParentingRules().clear();
	}

	public ShapeObjectType getDelayGate() {
		return this.DelayGate;
	}

	private void createAndGate() {
		this.AndGate.setDescription("AndGate");// ment to be TypeDescription
		// rather
		this.AndGate.getDefaultAttributes().setShapeDefinition(AND_SHAPE_DEFN);
		this.AndGate.getDefaultAttributes().setFillColour(
				new RGB(255, 255, 255));
		this.AndGate.getDefaultAttributes().setSize(new Dimension(30, 30));
		this.AndGate.getDefaultAttributes().setLineWidth(1);
		this.AndGate.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.AndGate.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.AndGate.setEditableAttributes(editableAttributes);
	}

	private void defineParentingAndGate() {
		this.AndGate.getParentingRules().clear();
	}

	public ShapeObjectType getAndGate() {
		return this.AndGate;
	}

	private void createOrGate() {
		this.OrGate.setDescription("OR Logical Operator");// ment to be
		// TypeDescription rather
		this.OrGate.getDefaultAttributes().setShapeDefinition(OR_SHAPE_DEFN);
		this.OrGate.getDefaultAttributes()
				.setFillColour(new RGB(255, 255, 255));
		this.OrGate.getDefaultAttributes().setSize(new Dimension(30, 30));
		this.OrGate.getDefaultAttributes().setLineWidth(1);
		this.OrGate.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.OrGate.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.OrGate.setEditableAttributes(editableAttributes);
	}

	private void defineParentingOrGate() {
		this.OrGate.getParentingRules().clear();
	}

	public ShapeObjectType getOrGate() {
		return this.OrGate;
	}

	private void createNotGate() {
		this.NotGate.setDescription("NOT Logical Operator");// ment to be
		// TypeDescription
		// rather
		this.NotGate.getDefaultAttributes().setShapeDefinition(NOT_SHAPE_DEFN);
		this.NotGate.getDefaultAttributes().setFillColour(RGB.WHITE);
		this.NotGate.getDefaultAttributes().setSize(new Dimension(30, 30));
		this.NotGate.getDefaultAttributes().setLineWidth(1);
		this.NotGate.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.NotGate.getDefaultAttributes().setLineColour(RGB.BLACK);

		EnumSet<EditableShapeAttributes> editableAttributes = EnumSet
				.noneOf(EditableShapeAttributes.class);
		if (true) {
			editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
		}
		if (true) {
			editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
		}
		this.NotGate.setEditableAttributes(editableAttributes);
	}

	private void defineParentingNotGate() {
		this.NotGate.getParentingRules().clear();
	}

	public ShapeObjectType getNotGate() {
		return this.NotGate;
	}

	private void createModulation() {
		Set<IShapeObjectType> set = null;
		this.Modulation
				.setDescription("A modulation affects the flux of a process represented by the target transition.");
		int[] lc = new int[] { 0, 0, 0 };
		this.Modulation.getDefaultAttributes().setLineWidth(1);
		this.Modulation.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Modulation.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.Modulation.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.Modulation.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.Modulation.getDefaultAttributes().setLineWidthEditable(true);
		this.Modulation.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.Modulation.getLinkSource();
		// LinkEndDefinition tport=this.Modulation.getLinkTarget();
		LinkTerminusDefinition sport = this.Modulation
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.Modulation
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.EMPTY_DIAMOND);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.Modulation.getLinkConnectionRules().addConnection(this.Entity,
					tgt);
			this.Modulation.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.Modulation.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.Modulation.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.Modulation.getLinkConnectionRules().addConnection(this.OrGate,
					tgt);
			this.Modulation.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.Modulation.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}
	}

	public LinkObjectType getModulation() {
		return this.Modulation;
	}

	private void createStimulation() {
		Set<IShapeObjectType> set = null;
		this.Stimulation
				.setDescription("A stimulation affects positively the flux of a process represented by the target transition.");
		int[] lc = new int[] { 0, 0, 0 };
		this.Stimulation.getDefaultAttributes().setLineWidth(1);
		this.Stimulation.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Stimulation.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.Stimulation.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.Stimulation.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.Stimulation.getDefaultAttributes().setLineWidthEditable(true);
		this.Stimulation.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.Stimulation.getLinkSource();
		// LinkEndDefinition tport=this.Stimulation.getLinkTarget();
		LinkTerminusDefinition sport = this.Stimulation
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.Stimulation
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.EMPTY_TRIANGLE);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.Entity, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.OrGate, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.Stimulation.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getStimulation() {
		return this.Stimulation;
	}

	private void createAnnotationArc() {
		Set<IShapeObjectType> set = null;
		this.AnnotationArc
				.setDescription("Annotation Arc is the arc used to connetc Annotation ot entity it annotates.");
		int[] lc = new int[] { 0, 0, 0 };
		this.AnnotationArc.getDefaultAttributes().setLineWidth(3);
		this.AnnotationArc.getDefaultAttributes().setLineStyle(LineStyle.DOT);
		this.AnnotationArc.getDefaultAttributes().setLineColour(RGB.BLACK);
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.LogicArc.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.LogicArc.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.LogicArc.getDefaultAttributes().setLineWidthEditable(true);
		this.AnnotationArc.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.LogicArc.getLinkSource();
		// LinkEndDefinition tport=this.LogicArc.getLinkTarget();
		LinkTerminusDefinition sport = this.AnnotationArc
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.AnnotationArc
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 0);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set
				.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
						this.OrGate, this.NotGate, this.DelayGate, this.Entity,
						this.Outcome, this.Observable, this.Perturbation,
						this.State }));
		for (IShapeObjectType tgt : set) {
			this.AnnotationArc.getLinkConnectionRules()
					.addConnection(this.Note, tgt);
		}

	}

	public LinkObjectType getAnnotationArc() {
		return this.AnnotationArc;
	}

	private void createLogicArc() {
		Set<IShapeObjectType> set = null;
		this.LogicArc
				.setDescription("Logic arc is the arc used to represent the fact that an entity influences outcome of logic operator.");
		int[] lc = new int[] { 0, 0, 0 };
		this.LogicArc.getDefaultAttributes().setLineWidth(1);
		this.LogicArc.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.LogicArc.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.LogicArc.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.LogicArc.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.LogicArc.getDefaultAttributes().setLineWidthEditable(true);
		this.LogicArc.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.LogicArc.getLinkSource();
		// LinkEndDefinition tport=this.LogicArc.getLinkTarget();
		LinkTerminusDefinition sport = this.LogicArc
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.LogicArc
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 0);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(this.Entity,
					tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(this.Outcome,
					tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(this.AndGate,
					tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(this.OrGate,
					tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(this.NotGate,
					tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.AndGate,
				this.OrGate, this.NotGate, this.DelayGate }));
		for (IShapeObjectType tgt : set) {
			this.LogicArc.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getLogicArc() {
		return this.LogicArc;
	}

	private void createInhibition() {
		Set<IShapeObjectType> set = null;
		this.Inhibition
				.setDescription("An inhibition affects negatively the flux of a process represented by the target transition.");
		int[] lc = new int[] { 0, 0, 0 };
		this.Inhibition.getDefaultAttributes().setLineWidth(1);
		this.Inhibition.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.Inhibition.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.Inhibition.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.Inhibition.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.Inhibition.getDefaultAttributes().setLineWidthEditable(true);
		this.Inhibition.setEditableAttributes(editableAttributes);

		LinkTerminusDefinition sport = this.Inhibition
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.Inhibition
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.BAR);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.Inhibition.getLinkConnectionRules().addConnection(this.Entity,
					tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(this.OrGate,
					tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.Inhibition.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getInhibition() {
		return this.Inhibition;
	}

	private void createAbsInhibition() {
		Set<IShapeObjectType> set = null;
		this.AbsInhibition
				.setDescription("An absolute inhibition block the flux of a process represented by the target transition.");
		int[] lc = new int[] { 0, 0, 0 };
		this.AbsInhibition.getDefaultAttributes().setLineWidth(1);
		this.AbsInhibition.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.AbsInhibition.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.AbsInhibition.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.AbsInhibition.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.AbsInhibition.getDefaultAttributes().setLineWidthEditable(true);
		this.AbsInhibition.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.AbsInhibition.getLinkSource();
		// LinkEndDefinition tport=this.AbsInhibition.getLinkTarget();
		LinkTerminusDefinition sport = this.AbsInhibition
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.AbsInhibition
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		// sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.DOUBLE_BAR);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.Entity, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.OrGate, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.AbsInhibition.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getAbsInhibition() {
		return this.AbsInhibition;
	}

	private void createNecessaryStimulation() {
		Set<IShapeObjectType> set = null;
		this.NecessaryStimulation
				.setDescription("A Necessary Stimulation effect, or absolute stimulation, is a stimulation that is necessary for a process to take place.");
		int[] lc = new int[] { 0, 0, 0 };
		this.NecessaryStimulation.getDefaultAttributes().setLineWidth(1);
		this.NecessaryStimulation.getDefaultAttributes().setLineStyle(
				LineStyle.SOLID);
		this.NecessaryStimulation.getDefaultAttributes().setLineColour(
				RGB.BLACK);
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.NecessaryStimulation.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.NecessaryStimulation.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.NecessaryStimulation.getDefaultAttributes().setLineWidthEditable(true);
		this.NecessaryStimulation.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.NecessaryStimulation.getLinkSource();
		// LinkEndDefinition tport=this.NecessaryStimulation.getLinkTarget();
		LinkTerminusDefinition sport = this.NecessaryStimulation
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.NecessaryStimulation
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap(0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		// sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.TRIANGLE_BAR);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.Entity, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.OrGate, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.NecessaryStimulation.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getNecessaryStimulation() {
		return this.NecessaryStimulation;
	}

	private void createAbsStimulation() {
		Set<IShapeObjectType> set = null;
		this.AbsStimulation
				.setDescription("A Absolute Stimulation effect, or absolute stimulation, is a stimulation that is necessary for a process to take place.");
		int[] lc = new int[] { 0, 0, 0 };
		this.AbsStimulation.getDefaultAttributes().setLineWidth(1);
		this.AbsStimulation.getDefaultAttributes()
				.setLineStyle(LineStyle.SOLID);
		this.AbsStimulation.getDefaultAttributes().setLineColour(RGB.BLACK);
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.NecessaryStimulation.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.NecessaryStimulation.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		this.AbsStimulation.setEditableAttributes(editableAttributes);

		LinkTerminusDefinition sport = this.AbsStimulation
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.AbsStimulation
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap(0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		// sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 5);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.DOUBLE_ARROW);// , 5,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(5, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Acceptor,
				this.Interaction, this.Observable }));
		for (IShapeObjectType tgt : set) {
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.Entity, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.Perturbation, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.AndGate, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.OrGate, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.NotGate, tgt);
			this.AbsStimulation.getLinkConnectionRules().addConnection(
					this.DelayGate, tgt);
		}

	}

	public LinkObjectType getAbsStimulation() {
		return this.AbsStimulation;
	}

	private void createInteractionArc() {
		Set<IShapeObjectType> set = null;
		this.InteractionArc.setDescription("Interaction arc is the arc.");
		int[] lc = new int[] { 0, 0, 0 };
		this.InteractionArc.getDefaultAttributes().setLineWidth(1);
		this.InteractionArc.getDefaultAttributes()
				.setLineStyle(LineStyle.SOLID);
		this.InteractionArc.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.InteractionArc.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.InteractionArc.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.InteractionArc.getDefaultAttributes().setLineWidthEditable(true);
		this.InteractionArc.setEditableAttributes(editableAttributes);

		LinkTerminusDefinition sport = this.InteractionArc
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.InteractionArc
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 0);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.ARROW);// , 7,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(7, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Entity,
				this.Outcome }));
		for (IShapeObjectType tgt : set) {
			this.InteractionArc.getLinkConnectionRules().addConnection(
					this.Interaction, tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Entity,
				this.Outcome }));
		for (IShapeObjectType tgt : set) {
			this.InteractionArc.getLinkConnectionRules().addConnection(
					this.Acceptor, tgt);
		}
		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Entity,
				this.Outcome }));
		for (IShapeObjectType tgt : set) {
			this.InteractionArc.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
		}

	}

	public LinkObjectType getInteractionArc() {
		return this.InteractionArc;
	}

	private void createAssignmenArc() {
		Set<IShapeObjectType> set = null;
		this.AssignmenArc.setDescription("Assignment arc is the arc.");
		int[] lc = new int[] { 0, 0, 0 };
		this.AssignmenArc.getDefaultAttributes().setLineWidth(1);
		this.AssignmenArc.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.AssignmenArc.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.AssignmenArc.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.AssignmenArc.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.AssignmenArc.getDefaultAttributes().setLineWidthEditable(true);
		this.AssignmenArc.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.AssignmenArc.getLinkSource();
		// LinkEndDefinition tport=this.AssignmenArc.getLinkTarget();
		LinkTerminusDefinition sport = this.AssignmenArc
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.AssignmenArc
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 0);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.ARROW);// , 7,5);
		tport.getDefaultAttributes().setEndSize(new Dimension(7, 5));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.ExState, this.State, this.LocState }));
		for (IShapeObjectType tgt : set) {
			this.AssignmenArc.getLinkConnectionRules().addConnection(
					this.State, tgt);
			this.AssignmenArc.getLinkConnectionRules().addConnection(
					this.Decision, tgt);
			this.AssignmenArc.getLinkConnectionRules().addConnection(
					this.Outcome, tgt);
			this.AssignmenArc.getLinkConnectionRules().addConnection(
					this.Acceptor, tgt);
		}

	}

	public LinkObjectType getAssignmenArc() {
		return this.AssignmenArc;
	}

	private void createVarArc() {
		Set<IShapeObjectType> set = null;
		this.VarArc
				.setDescription("Arc is the arc connecting state value to decision point or state variable to outcome or two outcomes or outcome to decision point.");
		int[] lc = new int[] { 0, 0, 0 };
		this.VarArc.getDefaultAttributes().setLineWidth(1);
		this.VarArc.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
		this.VarArc.getDefaultAttributes().setLineColour(
				new RGB(lc[0], lc[1], lc[2]));
		EnumSet<LinkEditableAttributes> editableAttributes = EnumSet
				.noneOf(LinkEditableAttributes.class);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.COLOUR);
		}
		// this.VarArc.getDefaultAttributes().setLineColourEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
		}
		// this.VarArc.getDefaultAttributes().setLineStyleEditable(true);
		if (true) {
			editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
		}
		// this.VarArc.getDefaultAttributes().setLineWidthEditable(true);
		this.VarArc.setEditableAttributes(editableAttributes);

		// LinkEndDefinition sport=this.VarArc.getLinkSource();
		// LinkEndDefinition tport=this.VarArc.getLinkTarget();
		LinkTerminusDefinition sport = this.VarArc
				.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = this.VarArc
				.getTargetTerminusDefinition();
		sport.getDefaultAttributes().setGap((short) 0);
		sport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		sport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editablesportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// sport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// sport.getDefaultAttributes().setColourEditable(true);
		sport.setEditableAttributes(editablesportAttributes);
		tport.getDefaultAttributes().setGap((short) 0);
		tport.getDefaultAttributes().setEndDecoratorType(
				LinkEndDecoratorShape.NONE);// , 8,8);
		tport.getDefaultAttributes().setEndSize(new Dimension(8, 8));
		EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet
				.of(LinkTermEditableAttributes.END_SIZE,
						LinkTermEditableAttributes.OFFSET,
						LinkTermEditableAttributes.TERM_DECORATOR_TYPE,
						LinkTermEditableAttributes.TERM_SIZE);
		if (true) {
			editabletportAttributes
					.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
		}
		// tport.getDefaultAttributes().setShapeTypeEditable(true);
		if (true) {
			editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
		}
		// tport.getDefaultAttributes().setColourEditable(true);
		tport.setEditableAttributes(editabletportAttributes);

		set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Decision,
				this.Outcome, this.Acceptor, this.ExState, this.LocState, this.State }));
		for (IShapeObjectType tgt : set) {
			this.VarArc.getLinkConnectionRules().addConnection(this.State, tgt);
			this.VarArc.getLinkConnectionRules().addConnection(this.Outcome,
					tgt);
			this.VarArc.getLinkConnectionRules().addConnection(this.Acceptor,
					tgt);
			this.VarArc.getLinkConnectionRules().addConnection(this.Decision,
					tgt);
		}

	}

	public LinkObjectType getVarArc() {
		return this.VarArc;
	}

}
