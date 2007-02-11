dojo.provide("tapestry.fx");

dojo.require("tapestry.core");

/**
 * package: tapestry.fx
 * Provides handling of effects applied before, during or after an XHR request/response.
 */
tapestry.fx={
    
    // property: preEffects
    // Contains a reference to all registered pre-effects, i.e. effects that are
    // executed before an XHR request.
    preEffects:{},
    // property: postEffects
    // Contains a reference to all registered post-effects, i.e. effects that are
    // executed when new content arrives through an XHR response.
    postEffects:{},    
    
    /**
     * Function: attachPreEffect
     * Schedules the execution of an effect when the specified link
     * is clicked (and thus an XHR request begins).
     * 
     * See Also:
     * 	<tapestry.fx.attachPostEffect>  
     *  <dojo.lfx.IAnimation> 
     *
     * Parameters:
     * 	triggerId - The clientId of the DirectLink that triggers the effect.
     *  animationFunc - A function that returns the animation to execute. 
     *  async - Boolean for whether to execute the effect in parallel to the
     *          XHR request. Defaults to false, i.e. the XHR is blocked until
     *          the effect ends.
     * 
     * Note:
     * 	Here's an example usage:
     *      tapestry.fx.attachPreEffect("DirectLink",
     *          function(){return dojo.lfx.wipeOut("entry", 800, dojo.lfx.easeDefault) });
     */   
    attachPreEffect:function(triggerId, animationFunc, async){
        if (dojo.lang.isEmpty(this.preEffects))
            this._initPreEffects();
        this.preEffects[triggerId] = {async:async, animation:animationFunc};        
    },
    
    /**
     * Function: attachPostEffect
     * Schedules the execution of an effect when the specified content
     * is returned through an XHR response.
     * 
     * See Also:
     * 	<tapestry.fx.attachPreEffect>  
     *  <dojo.lfx.IAnimation> 
     *
     * Parameters:
     * 	updateId - The id of a dom node that (when updated) triggers the effect.
     *  animationFunc - A function that returns the animation to execute. 
     * 
     * Note:
     * 	Here's an example usage:
     *      tapestry.fx.attachPostEffect("entry",
     *          function(){return dojo.lfx.wipeIn("entry", 1500, dojo.lfx.easeDefault) });
     */       
    attachPostEffect:function(updateId, animationFunc){
        if (dojo.lang.isEmpty(this.postEffects))
            this._initPostEffects();        
        this.postEffects[updateId] = {animation:animationFunc};
    },
    
    /**
     * Function: removeAll
     * Removes all registered effects (preEffects and postEffects).
     */       
    removeAll:function(){
        this.preEffects={};
        this.postEffects={};
    },
    
    _initPreEffects:function(){
        dojo.debug("Advising tapestry.linkOnClick");
        dojo.event.connectAround(tapestry, "linkOnClick", tapestry.fx, "_applyPreEffects");
    },
    
    _initPostEffects:function(){
        dojo.debug("Advising tapestry.loadContent");
        dojo.event.connectAround(tapestry, "loadContent", tapestry.fx, "_applyPostEffects");
    },
    
    _applyPreEffects:function(miObj){
        var id = miObj.args[1];        
        var effect = this.preEffects[id];
        if (effect){
            dojo.debug("Found pre-effect:", effect, id);
                       
            var anim = effect.animation();
            
            if (effect.async){          
                anim.play();
                return miObj.proceed();                
            }
            else{
                anim.connect("onEnd", function(){ miObj.proceed(); });
                anim.play();
                return false;
            }
        }
        else{
            return miObj.proceed();
        }        
    },
    
    _applyPostEffects:function(miObj){
        var id = miObj.args[0];
        var effect = this.postEffects[id];
        if (effect){
            dojo.debug("Found post-effect:", effect, id);
            
            var ret = miObj.proceed();
            
            var anim = effect.animation();
            anim.play();
            
            return ret;
        }
        else{            
            return miObj.proceed();
        }        
    }
}
