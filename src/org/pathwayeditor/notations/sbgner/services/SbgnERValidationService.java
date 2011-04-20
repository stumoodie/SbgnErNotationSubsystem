/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
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
