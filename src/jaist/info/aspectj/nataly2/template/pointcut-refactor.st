
  $\n$
  pointcut $pt_name$ : $\n$ 
    $statement$    
    && if($condition_name$(thisJoinPoint)); 

  static boolean $condition_name$(JoinPoint shadow) {      
        TPattern pattern =new TPattern("$npt_name$");
        TMatcher matcher=pattern.match(shadow);
        return matcher.ismatch();

 } $\n$

