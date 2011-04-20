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

import java.util.Collections;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.IModel;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Version;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationAutolayoutService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationConversionService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationExportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationImportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationPluginService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.notationsubsystem.toolkit.definition.GeneralNotation;

public class SbgnERNotationSubsystem implements INotationSubsystem {
	private static final String GLOBAL_ID = "org.pathwayeditor.notations.sbgner";
	private static final String DESCRIPTION = "SBGN Entity Relationship Notation";
	private static final String NAME = "SBGN-ER";
	private static final Version VERSION = new Version(0, 9, 1);
	private SbgnERNotationSyntaxService syntaxService;
	private SbgnERValidationService validationService;
	private INotation context;

	public SbgnERNotationSubsystem() {
		this.context = new GeneralNotation(GLOBAL_ID, NAME, DESCRIPTION, VERSION);
		this.syntaxService = new SbgnERNotationSyntaxService(this);
		this.validationService=new SbgnERValidationService(this);
	}
	

	public INotation getNotation() {
		return this.context;
	}

	public Set<INotationExportService> getExportServices() {
		return Collections.emptySet();
	}

	public Set<INotationImportService> getImportServices() {
		return Collections.emptySet();
	}

	public Set<INotationPluginService> getPluginServices() {
		return Collections.emptySet();
	}

	public SbgnERNotationSyntaxService getSyntaxService() {
		return this.syntaxService;
	}

	public INotationValidationService getValidationService() {
		return validationService;
	}

	public Set<INotationConversionService> getConversionServices() {
		return Collections.emptySet();
	}

	public INotationAutolayoutService getAutolayoutService() {
		return new DefaultAutolayoutService();
	}


	private class DefaultAutolayoutService implements INotationAutolayoutService {

		public boolean isImplemented() {
			return false;
		}
        public void layout(IModel canvas) {
            throw new UnsupportedOperationException("Notation subsystem does not support this operation");
        }

        public INotation getNotation() {
            return SbgnERNotationSubsystem.this.getNotation();
        }

        public INotationSubsystem getNotationSubsystem() {
            return SbgnERNotationSubsystem.this;
        }

	}

    public boolean isFallback() {
        return false;
    }


	@Override
	public void registerModel(IModel modelToRegister) {
	}


	@Override
	public void unregisterModel(IModel modelToRegister) {
	}
}
