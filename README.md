The project will build an amazing robot for concumser to control remotely.  
The robot can be used in different scenarios, like property inspections remotely, parents control, exploring in dangerous environment and so on…… 
 
- web-app  
  The web-app enable consumer to control the robot:  
   * Move Forward
   * Move Back
   * Speed Up
   * Show Video        
 

- android-app  
  The android's app receives command from consumer and send command to robot.
   * Share video with consumer
   * Receive command from consumer
   * Assign command to Ardunio
   
   

- ardunio-app  
   The adrunio app will control the robot.
   * Receive command by USB
   * Control the physical box by analog signal

===================================


###Install Stun Server  
By default, webrtc uses google stun servers which are not stable, so we need to install a new stun server by ourselves.

Please install [stunserver](https://github.com/jselbie/stunserver)
