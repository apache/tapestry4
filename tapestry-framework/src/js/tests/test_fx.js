dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("tapestry.fx");
dojo.require("dojo.lfx");

function test_preEffects(){
    
        tapestry.fx.removeAll();
        
        jum.assertTrue(dojo.lang.isEmpty(tapestry.fx.preEffects));
        
        var animation = dojo.lfx.wipeIn();
        
        tapestry.fx.attachPreEffect("div1", animation);
        
        jum.assertFalse(dojo.lang.isEmpty(tapestry.fx.preEffects));
        
        jum.assertEquals(animation, tapestry.fx.preEffects["div1"].animation);
        
}

function test_postEffects(){
    
        tapestry.fx.removeAll();
        
        jum.assertTrue(dojo.lang.isEmpty(tapestry.fx.postEffects));
        
        tapestry.fx.attachPostEffect("div1", dojo.lfx.wipeIn());
        
        jum.assertFalse(dojo.lang.isEmpty(tapestry.fx.postEffects));
}