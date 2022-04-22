package utils;

import org.tzi.use.api.UseSystemApi;
import org.tzi.use.uml.mm.MAttribute;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.ocl.value.*;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MObjectState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pérez - University of Málaga
 * A collection of utility methods for USE model manipulation.
 */
public class UseFacade {

    private final UseSystemApi api;

    /**
     * Sets the API instance to use for all subsequent calls to UseFacade methods.
     * @param api The USE API instance to interact with the currently displayed object diagram.
     */
    public UseFacade(UseSystemApi api) {
        this.api = api;
    }

    public void updateDerivedValues() {
        api.getSystem().state().updateDerivedValues(true);
    }

    // Object Searching
    // ============================================================================================

    /**
     * Returns all objects of a specific class in the USE model.
     * @param className The name of the class whose instances to retrieve.
     * @return A list with all objects of the specified class.
     */
    public List<MObjectState> getObjectsOfClass(String className) {
        List<MObjectState> result = new ArrayList<>();
        MClass mclass = api.getSystem().model().getClass(className);
        for (MObject o : api.getSystem().state().allObjects()) {
            if (o.cls().allSupertypes().contains(mclass)) {
                MObjectState ostate = o.state(api.getSystem().state());
                result.add(ostate);
            }
        }
        return result;
    }

    /**
     * Returns any object of a specific class in the USE model.
     * @param className The name of the class whose instance to retrieve.
     * @return An instance of the given class, or null if no instances are found.
     */
    public MObjectState getAnyObjectOfClass(String className) {
        MObjectState result = null;
        MClass mclass = api.getSystem().model().getClass(className);
        for (MObject o : api.getSystem().state().allObjects()) {
            if (o.cls().allSupertypes().contains(mclass)) {
                result = (o.state(api.getSystem().state()));
                break;
            }
        }
        return result;
    }

    // Attribute Getters
    // ============================================================================================

    /**
     * Returns the value of an integer attribute in the model.
     * @param objstate The state of the object whose attribute to retrieve.
     * @param attributeName The name of the attribute to retrieve.
     * @return The value of the attribute, or null if the value is not an integer.
     */
    public Integer getIntegerAttribute(MObjectState objstate, String attributeName) {
        Value v = objstate.attributeValue(attributeName);
        return v instanceof IntegerValue ? ((IntegerValue) v).value() : null;
    }

    /**
     * Returns the value of a real number attribute in the model.
     * @param objstate The state of the object whose attribute to retrieve.
     * @param attributeName The name of the attribute to retrieve.
     * @return The value of the attribute, or null if the value is not a real number.
     */
    public Double getRealAttribute(MObjectState objstate, String attributeName) {
        Value v = objstate.attributeValue(attributeName);
        return v instanceof RealValue ? ((RealValue) v).value() : null;
    }

    /**
     * Returns the value of a string attribute in the model.
     * @param objstate The state of the object whose attribute to retrieve.
     * @param attributeName The name of the attribute to retrieve.
     * @return The value of the attribute, or null if the value is not a string.
     */
    public String getStringAttribute(MObjectState objstate, String attributeName) {
        Value v = objstate.attributeValue(attributeName);
        return v instanceof StringValue ? ((StringValue) v).value() : null;
    }

    /**
     * Returns the value of a boolean attribute in the model.
     * @param objstate The state of the object whose attribute to retrieve.
     * @param attributeName The name of the attribute to retrieve.
     * @return The value of the attribute, or null if the value is not a boolean value.
     */
    public Boolean getBooleanAttribute(MObjectState objstate, String attributeName) {
        Value v = objstate.attributeValue(attributeName);
        return v instanceof BooleanValue ? ((BooleanValue) v).value() : null;
    }

    /**
     * Returns the value of any attribute as a string.
     * @param objstate The state of the object whose attribute to retrieve.
     * @param attributeName The name of the attribute to retrieve.
     * @return The value of the attribute.
     */
    public String getAttributeAsString(MObjectState objstate, String attributeName) {
        return objstate.attributeValue(attributeName).toString();
    }

    // Attribute Setters
    // ============================================================================================

    /**
     * Sets the value of attribute <i>attributeName</i>.
     * @param objstate The object whose attribute to set.
     * @param attributeName The name of the attribute to set.
     * @param value The value to set.
     */
    public void setAttribute(MObjectState objstate, String attributeName, int value) {
        setAttributeAux(objstate, attributeName, IntegerValue.valueOf(value));
    }

    /**
     * Sets the value of attribute <i>attributeName</i>.
     * @param objstate The object whose attribute to set.
     * @param attributeName The name of the attribute to set.
     * @param value The value to set.
     */
    public void setAttribute(MObjectState objstate, String attributeName, double value) {
        setAttributeAux(objstate, attributeName, new RealValue(value));
    }

    /**
     * Sets the value of attribute <i>attributeName</i>.
     * @param objstate The object whose attribute to set.
     * @param attributeName The name of the attribute to set.
     * @param value The value to set.
     */
    public void setAttribute(MObjectState objstate, String attributeName, String value) {
        setAttributeAux(objstate, attributeName, new StringValue(value));
    }

    /**
     * Sets the value of attribute <i>attributeName</i>.
     * @param objstate The object whose attribute to set.
     * @param attributeName The name of the attribute to set.
     * @param value The value to set.
     */
    public void setAttribute(MObjectState objstate, String attributeName, boolean value) {
        setAttributeAux(objstate, attributeName, BooleanValue.get(value));
    }

    private void setAttributeAux(MObjectState objstate, String attributeName, Value value) {
        MClass mclass = objstate.object().cls();
        MAttribute attribute = mclass.attribute(attributeName, true);
        objstate.setAttributeValue(attribute, value);
    }

}
