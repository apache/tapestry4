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

import java.util.Map;

/**
 *  Quiz deck for the Quiz application.
 *
 *  @version $Id$
 *  @author David Solis
 *
 **/

public abstract class Quiz extends Deck
{
    int questionNumber;

    public void fetch()
    {
        Visit visit = (Visit) getVisit();
        Global global = (Global) getGlobal();
        Map question = global.getQuestion(getQuestionNumber(), visit.getQuestionSet());
        setQuestion(question);
    }

    public IPropertySelectionModel getChoiceModel()
    {
        return new StringPropertySelectionModel((String[])getQuestion().get(Global.CHOICES_KEY));
    }

    public void process(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();
        int rightAnswer = ((Integer)getQuestion().get(Global.ANSWER_KEY)).intValue();
        if (getChoice() == rightAnswer)
        {
            int points = visit.getPoints();
            points += visit.getQuestionSet();
            visit.setPoints(points);
        }
        int questionNumber = getQuestionNumber() + 1;
        if (questionNumber < visit.getNumberOfQuestions())
        {
            setQuestionNumber(questionNumber);
            fetch();
        }
        else
        {
            Scores scoresDeck = (Scores)cycle.getPage("Scores");
            Global global = (Global) getGlobal();
            boolean isNewHighscore = global.addHighscore(visit.getPoints(), visit.getUsername()) != -1;
            scoresDeck.setNewHighscore(isNewHighscore);
            scoresDeck.setHighscores(global.getHighscores());
            cycle.activate(scoresDeck);
        }
    }

    protected void initialize()
    {
        questionNumber = 0;
    }

    public int getQuestionNumber()
    {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber)
    {
        this.questionNumber = questionNumber;
        fireObservedChange("questionNumber", questionNumber);
    }

    public abstract Map getQuestion();
    public abstract void setQuestion(Map question);

    public abstract int getChoice();


}
