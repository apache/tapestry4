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

import java.io.Serializable;

/**
 *  Visit object for the Quiz application.
 *
 *  @version $Id$
 *  @author David Solis
 *
 **/

public class Visit implements Serializable
{
    private String username;
    private String level;
    private int points;
    private int questionSet;
    private int numberOfQuestions;

    public void initialize(String username, String level, int questionSet, int numberOfQuestions)
    {
        this.username = username;
        this.level = level;
        this.questionSet = questionSet;
        this.numberOfQuestions = numberOfQuestions;
        points = 0;
    }

    public int getNumberOfQuestions()
    {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions)
    {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getQuestionSet()
    {
        return questionSet;
    }

    public void setQuestionSet(int questionSet)
    {
        this.questionSet = questionSet;
    }

    public void incPoints()
    {
        points++;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

	public String getUsername()
    {
		return username;
	}

	public void setUsername(String user)
    {
		this.username = user;
	}

}
