//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.wap.quiz;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.wml.Deck;

/**
 *  Home deck for the Quiz application.
 *
 *  Quiz application is a game that is run on a server that people can connect to using a WAP phone.
 *  The game is highly customizable. You can add your own questions to simple text files without
 *  any knowledge of programming.
 *
 *  @version $Id$
 *  @author David Solis
 *
 **/

public abstract class Home extends Deck
{

	public static final IPropertySelectionModel LEVEL_MODEL = new StringPropertySelectionModel
            (new String[] { Global.EASY_LEVEL, Global.MEDIUM_LEVEL, Global.HARD_LEVEL });
	
	public void start(IRequestCycle cycle)
	{
        Global global = (Global) getGlobal();
        global.initialize(cycle);
        Visit visit = (Visit) getVisit();
        String level = getLevel();
        int questionSet = global.getQuestionSet(level);
        String username = getUsername();
        if (username == null || "".equals(username))
            username = "newcomer";
        visit.initialize(username, level, questionSet, global.getNumberOfQuestions(questionSet));
        Quiz quizDeck = (Quiz) cycle.getPage("Quiz");
        quizDeck.setQuestionNumber(0);
        quizDeck.fetch();
        cycle.activate(quizDeck);
	}
	
	public abstract String getUsername();
	public abstract void setUsername(String username);
	
	public abstract String getLevel();
	public abstract void setLevel(String level);
}
