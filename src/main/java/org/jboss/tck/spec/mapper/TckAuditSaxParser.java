/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.tck.spec.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jboss.tck.spec.audit.Assertion;
import org.jboss.tck.spec.audit.Group;
import org.jboss.tck.spec.audit.Section;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Tomas Remes
 */
public class TckAuditSaxParser extends DefaultHandler {

    List<Section> sections;
    List<Group> groups;

    private StringBuffer tmpVal;
    private Section tmpSection;
    private Assertion tmpAssertion;
    private Group tmpGroup;
    private String xmlFilePath;
    private boolean groupTextExist;
    private static final Logger log = Logger.getLogger(TckAuditSaxParser.class.getName());

    public TckAuditSaxParser(String xmlFilePath) {
        this.sections = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.xmlFilePath = xmlFilePath;
    }

    public void parseDocument() {

        log.fine("Starting parsing of audit file.");
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            SAXParser sp = spf.newSAXParser();
            sp.parse(xmlFilePath, this);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            new Exception("Cannot find " + xmlFilePath + " :" + ie.getStackTrace().toString());
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tmpVal = new StringBuffer();

        if (qName.equalsIgnoreCase("section")) {
            tmpSection = new Section();
            tmpSection.setId(attributes.getValue("id"));
            tmpSection.setTitle(attributes.getValue("title"));
            tmpSection.setLevel(Integer.valueOf(attributes.getValue("level")));
        } else if (qName.equalsIgnoreCase("group")) {
            tmpGroup = new Group();
            groupTextExist = true;
        } else if (qName.equals("assertion")) {
            tmpAssertion = new Assertion();
            if (attributes.getValue("id") != null) {
                tmpAssertion.setId(attributes.getValue("id"));
            }
            if (attributes.getValue("testable") != null) {
                tmpAssertion.setTestable(Boolean.valueOf(attributes.getValue("testable")));
            }
            if (tmpGroup != null) {
                tmpGroup.getAssertions().add(tmpAssertion);
                tmpAssertion.setGroup(tmpGroup);
            }else {
                tmpSection.getSectionElements().add(tmpAssertion);
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tmpVal.append(new String(ch, start, length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("section")) {
            sections.add(tmpSection);

        } else if (qName.equalsIgnoreCase("text") && !groupTextExist) {

            tmpAssertion.setText(tmpVal.toString().replace("|", "").replace("_", "").replace("~", ""));
            //replaceAll("Section [0-9]?[0-9]\\.[0-9]*\\.*[0-9]*\\.*, ", ""));
        } else if (qName.equalsIgnoreCase("note")) {
            tmpAssertion.setNote(tmpVal.toString());
        } else if (qName.equalsIgnoreCase("text") && groupTextExist) {

            tmpGroup.setText(tmpVal.toString().replace("|", "").replace("_", "").replace("~", ""));
            groupTextExist = false;
        } else if(qName.equalsIgnoreCase("group")){
            //groups.add(tmpGroup);
            tmpSection.getSectionElements().add(tmpGroup);
            tmpGroup = null;
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
