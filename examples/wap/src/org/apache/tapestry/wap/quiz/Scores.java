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

import org.apache.tapestry.wml.Deck;

import java.util.List;

/**
 *  Scores deck for the Quiz application.
 *
 *  @version $Id$
 *  @author David Solis
 *
 **/

public abstract class Scores extends Deck
{

    public abstract List getHighscores();
    public abstract void setHighscores(List value);

    public abstract boolean isNewHighscore();
    public abstract void setNewHighscore(boolean value);
}
