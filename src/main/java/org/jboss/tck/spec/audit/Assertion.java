/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tck.spec.audit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Remes
 */
public class Assertion extends SectionElement {

    private String id = "";
    private String note;
    private boolean testable = true;
    private Group group;
    private List<Test> tests;

    public Assertion() {
        this.tests = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Test> getTests() {
        return tests;
    }

    public boolean isTestable() {
        return testable;
    }

    public void setTestable(boolean testable) {
        this.testable = testable;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.getText();
    }

}
