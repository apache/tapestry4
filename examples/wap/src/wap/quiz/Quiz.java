/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package wap.quiz;

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
