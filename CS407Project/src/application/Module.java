package application;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String moduleCode;
    private ArrayList<Class> classes;
    private int moduleId;

    public Module(String moduleCode) {
        this.moduleCode = moduleCode;
        this.classes = new ArrayList<>();
    }

    // Getter and setter for module code
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public int getModuleId() {
    	return moduleId;
    }
    public void setModuleId(int id) {
    	this.moduleId = id;
    }

    // Getter and setter for classes
    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    // Add a class to the module
    public void addClass(Class classObj) {
        this.classes.add(classObj);
    }
}
