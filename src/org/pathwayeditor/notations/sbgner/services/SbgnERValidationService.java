package org.pathwayeditor.notations.sbgner.services;

import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;

public class SbgnERValidationService implements INotationValidationService {

    private final INotationSubsystem serviceProvider;

    public SbgnERValidationService(INotationSubsystem provider) {
        this.serviceProvider = provider;
    }

    public INotationSubsystem getServiceProvider() {
        return serviceProvider;
    }

    public ICanvas getCanvasBeingValidated() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public IValidationReport getValidationReport() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public boolean hasBeenValidated() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public boolean hasWarnings() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public boolean isImplemented() {
        return false;
    }

    public boolean isValid() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public boolean isReadyToValidate() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public void setCanvasToValidate(ICanvas mapToValidate) {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public void validate() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public Set<IValidationRuleDefinition> getRules() {
        throw new UnsupportedOperationException(
                "Validation service has not been implemented for this notation subsystem");
    }

    public INotation getNotation() {
        return this.serviceProvider.getNotation();
    }

    public INotationSubsystem getNotationSubsystem() {
        return this.serviceProvider;
    }

}
