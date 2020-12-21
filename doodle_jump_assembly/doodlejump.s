# CSC258 final project
# student: Yuhao Yang
# id: 1005808057
# TA: Mr. Pooya

# ============
# ============
# ============

# need to know !!!!
#
# You need to connect screen and keyboard to play this game.
# You can press j or k to let doodle move to left to right.
# You can press h to restart the game if you died in game.
# 
# Since the project has a lot of duplicated, i.e. a lot of codes are reused time to time,
# please contact me martinyyh.yang@mail.utoronto.ca if you have any problem on it.
# Some lines are not commented since thoses are duplicated code that comment in somewhere of the project already.
# 
# Also the doodlke can jump accros the screen, for example if the doodle at the very right side of the screen and 
# you still move the doodle to right, then the doodle will across the screen and appear at the very left side of the screem.
# 
#
# Enjoy you time and have fun!
# ============
# ============
# ============


.data
	displayAddress: .word 0x10008000   	# start address
	display: .word 0x10008000 		# copy of start address
	endAddress: .word 0x10009000    	# end address of the screen
	end: .word 0x10009000  			# end copy
	doodle: .word 0x10008000  		# doole1 address
	doodle_copy: .word 0x00000000		# doole_copy address
	platform1: .word 0x10008000  		# platform1
	platform2: .word 0x10008000		# platform2
	platform3: .word 0x10008000		# platform3
	platform4: .word 0x10008000		# platform4
	paint: .word 0x10008000			# wow
	
 
.text
 	li $t3, 0xFFC0CB     		# load color pink to $t3
 	li $t5  0x0080FF     		# load color light-blue to $t3
 	li $t6  0x00FF00     		# load color green to $t6
 	li $t2  0xCC6600     		# load color brown to $t2
 	li $s7  0xFFFFF			# load color white to $s7
 	
 	
 	
Begin: 
 	lw $t0, displayAddress          # load start display address to $t0
 	lw $t4, endAddress     		# load end display address to $s4
  	lw $t1, doodle   		# load doodle to t1
  
  	addi $t8, $zero, 5  		# counter number from 4
  	lw $t7, displayAddress  	# set a start address from platform
  	addi $t7, $t7, 768  		# start from the 7th line
  	
  	lw $s7, display
	lw $s6, end
  	
Screen:  
	beq $t0, $t4, NewDoodle   	 # if start == end, exit 
	sw $t3, 0($t0)    		 # load color into register
	addi $t0, $t0, 4    		 # move to next register
	bne $t0, $t4, Screen   		 # if start != end, back to loop


  
Platform:

# 1 -------------------------------

	li $v0, 42  		# make a x-random
	li $a0, 0  		# ...
	li $a1, 23  		# ...
	syscall   		# ...
  
 	sll $a0, $a0, 2 	 # x times 4 from units to bytes 
  
	add $t7, $t7, $a0 	# update address(plus x)
	
	sw  $t7, platform1  ####################################
	
  
	sw $t2, 0($t7)  	# draw platform
  	sw $t2, 4($t7)  	# ...
  	sw $t2, 8($t7)  	# ...
 	sw $t2, 12($t7) 	 # ...
 	sw $t2, 16($t7) 	 # ...
 	sw $t2, 20($t7) 	 # ...
  	sw $t2, 24($t7) 	 # ...
  	sw $t2, 28($t7)  	# draw platform
  
  	sub $t7, $t7, $a0 	# let $t7 back to every unit in random line
  	sub $a0, $a0, $a0
  
  	addi $t7, $t7, 896 	 # move down 7 line vertically
  	
# 2 -------------------------------
   	
  	li $v0, 42  	# make a x-random
	li $a0, 0  	# ...
	li $a1, 23  	# ...
	syscall   	# ...
  
 	sll $a0, $a0, 2  # x times 4 from units to bytes 
  
	add $t7, $t7, $a0 # update address(plus x)
	
	sw  $t7, platform2   ####################
  
	sw $t2, 0($t7)  # draw platform
  	sw $t2, 4($t7)  # ...
  	sw $t2, 8($t7)  # ...
 	sw $t2, 12($t7)  # ...
 	sw $t2, 16($t7)  # ...
 	sw $t2, 20($t7)  # ...
  	sw $t2, 24($t7)  # ...
  	sw $t2, 28($t7)  # draw platform
  
  	sub $t7, $t7, $a0 # let $t7 back to every unit in random line
  	sub $a0, $a0, $a0
  
  	addi $t7, $t7, 896  # move down 7 line vertically
  	
# 3 -------------------------------
  	
  	li $v0, 42  	# make a x-random
	li $a0, 0  	# ...
	li $a1, 23  	# ...
	syscall   	# ...
  
 	sll $a0, $a0, 2  # x times 4 from units to bytes 
  
	add $t7, $t7, $a0 # update address(plus x)
	
	sw  $t7, platform3 #################################
  
	sw $t2, 0($t7)  	# draw platform
  	sw $t2, 4($t7)  	# ...
  	sw $t2, 8($t7)  	# ...
 	sw $t2, 12($t7) 	 # ...
 	sw $t2, 16($t7)  	# ...
 	sw $t2, 20($t7)  	# ...
  	sw $t2, 24($t7)  	# ...
  	sw $t2, 28($t7)  	# draw platform
  
  	sub $t7, $t7, $a0 	# let $t7 back to every unit in random line
  	sub $a0, $a0, $a0
  
  	addi $t7, $t7, 896 	 # move down 7 line vertically
  	
# 4 -------------------------------
  	
  	li $v0, 42  		# make a x-random
	li $a0, 0  		# ...
	li $a1, 23  		# ...
	syscall   		# ...
  
 	sll $a0, $a0, 2  	# x times 4 from units to bytes 
  
	add $t7, $t7, $a0 	# update address(plus x)
	
	sw  $t7, platform4 	
  
	sw $t2, 0($t7) 		# draw platform--0
  	sw $t2, 4($t7) 	 	# draw platform--1
  	sw $t2, 8($t7)  	# draw platform--2
 	sw $t2, 12($t7) 	# draw platform--3
 	sw $t2, 16($t7)  	# draw platform--4
 	sw $t2, 20($t7)  	# draw platform--5
  	sw $t2, 24($t7)  	# draw platform--6
  	sw $t2, 28($t7)  	# draw platform--7
  
  	sub $t7, $t7, $a0 	# let $t7 back to every unit in random line
  	sub $a0, $a0, $a0
  
  	addi $t7, $t7, 896  	# move down 7 line vertically
 
# -------------------------------
  
  
NewDoodle:

        lw   $t1, platform4		# load platform
        addi $t1, $t1, -368		# find the top location of doodle
        
        sw   $t1, doodle		# draw doodle again on the platform  --0 
        sw   $t5, 0($t1)		# draw doodle again on the platform  --1
        sw   $t5, 124($t1)		# draw doodle again on the platform   --2
        sw   $t5, 128($t1)		# draw doodle again on the platform   --3
        sw   $t5, 132($t1)		# draw doodle again on the platform   --4
        sw   $t5, 252($t1)		# draw doodle again on the platform   ---5
        sw   $t5, 260($t1)		# draw doodle again on the platform -- finished
        	
BeginJump:
        li   $v0, 32               	 #sleep
        li   $a0, 80		   	 #sleep
        syscall
        
        addi $s1, $zero, 12  		# doodle jump max number
  	addi $s0, $zero, 0  		# doodle jump now number
  	
# from *** to another *** to is to check the keyboard, this is the start 
# ==================================================================
# ==================================================================
# ==================================================================
# ==================================================================
	
NewJump:
        lw   $t8, 0xffff0000
        beq  $t8, 1, KeyboardJump
        sw   $zero, 0xffff0000
        J Jump
KeyboardJump:
        lw   $t8, 0xffff0004
        beq  $t8, 0x6a, KeyJJump
        beq  $t8, 0x6b, KeyKJump
        J Jump
KeyJJump:  
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, -4
        sw   $t1, doodle
        
        J Jump
KeyKJump:   
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, 4
        sw   $t1, doodle
        J Jump
        

# from *** to another *** to is to check the keyboard, this is the end
# ==================================================================
# ==================================================================
# ==================================================================
# ==================================================================

Jump:
        lw   $t1, doodle	# restore background color		
        sw   $t3, 0($t1)	# restore background color	
        sw   $t3, 124($t1)	# restore background color	
        sw   $t3, 128($t1)	# restore background color	
        sw   $t3, 132($t1)	# restore background color	
        sw   $t3, 252($t1)	# restore background color	
        sw   $t3, 260($t1)	# restore background color	
        
        addi $t1, $t1, -128    # move doodle one line up
        sw   $t1, doodle	# restore doodle color	
        sw   $t5, 0($t1)	# restore doodle color	
        sw   $t5, 124($t1)	# restore doodle color	
        sw   $t5, 128($t1)	# restore doodle color	
        sw   $t5, 132($t1)	# restore doodle color	
        sw   $t5, 252($t1)	# restore doodle color	
        sw   $t5, 260($t1)	# restore doodle color	
        
# draw platform is to make sure doodle will not unit of platform,
#so we need to restore platform color everytime when doodle jump

        lw $t1, platform1     # paint platform1
        sw $t2, 0($t1)  	# paint platform1
  	sw $t2, 4($t1)  	# paint platform1
  	sw $t2, 8($t1)  	# paint platform1
 	sw $t2, 12($t1)  	# paint platform1
 	sw $t2, 16($t1)  	# paint platform1
 	sw $t2, 20($t1)  	# paint platform1
  	sw $t2, 24($t1)  	# paint platform1
  	sw $t2, 28($t1) 	# paint platform1
  	
  	lw $t1, platform2     # paint platform2
        sw $t2, 0($t1)  	# paint platform2
  	sw $t2, 4($t1)  	# paint platform2
  	sw $t2, 8($t1)  # paint platform2
 	sw $t2, 12($t1)  # paint platform2
 	sw $t2, 16($t1)  # paint platform2
 	sw $t2, 20($t1)  # paint platform2
  	sw $t2, 24($t1)  # paint platform2
  	sw $t2, 28($t1)  # paint platform2
  	sw $t2, 28($t1)# paint platform2
  	
  	lw $t1, platform3     # paint platform3
        sw $t2, 0($t1)  # paint platform3
  	sw $t2, 4($t1)  # paint platform3
  	sw $t2, 8($t1)  # paint platform3
 	sw $t2, 12($t1)  # paint platform3
 	sw $t2, 16($t1)  # paint platform3
 	sw $t2, 20($t1)  # paint platform3
  	sw $t2, 24($t1)  # paint platform3
  	sw $t2, 28($t1)# paint platform3
  	sw $t2, 28($t1)# paint platform3
  	
  	lw $t1, platform4     # paint platform4
        sw $t2, 0($t1)  # paint platform4
  	sw $t2, 4($t1)  # paint platform4
  	sw $t2, 8($t1)  # paint platform4
 	sw $t2, 12($t1)  # paint platform4
 	sw $t2, 16($t1)  # paint platform4
 	sw $t2, 20($t1)  # paint platform4
  	sw $t2, 24($t1)  # paint platform4
  	sw $t2, 28($t1)# paint platform4
  	sw $t2, 28($t1)# paint platform4
        
        li   $v0, 32                #sleep
        li   $a0, 80			#sleep 
        syscall				# system call
        
        addi $s0, $s0, 1			# add counter
        beq  $s0, $s1, NewFall		# if the doodle gets the max height then fall down
        J NewJump

# ==================================================================
# ==================================================================
# ==================================================================
# ==================================================================
# from *** to another *** to is to check the keyboard, this is the start

NewFall:
        lw   $t8, 0xffff0000
        beq  $t8, 1, KeyboardFall
        sw   $zero, 0xffff0000
        J Fall
KeyboardFall:
        lw   $t8, 0xffff0004
        beq  $t8, 0x6a, KeyJFall
        beq  $t8, 0x6b, KeyKFall
        J Fall
KeyJFall:  
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, -4
        sw   $t1, doodle
        
        J Fall
KeyKFall:   
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, 4
        sw   $t1, doodle
        J Fall

# from *** to another *** to is to check the keyboard, this is the end
# ==================================================================
# ==================================================================
# ==================================================================
# ==================================================================
            
Fall:
        lw   $t1, doodle		# get the doodle location		
        sw   $t3, 0($t1)		# restore background color	
        sw   $t3, 124($t1)		# restore background color	
        sw   $t3, 128($t1)		# restore background color	
        sw   $t3, 132($t1)		# restore background color	
        sw   $t3, 252($t1)		# restore background color	
        sw   $t3, 260($t1)		# restore background color	
        
        addi $t1, $t1, 128		# move down one line
        sw   $t1, doodle		# update location
        sw   $t5, 0($t1)		# draw doodle
        sw   $t5, 124($t1)		# draw doodle
        sw   $t5, 128($t1)		# draw doodle
        sw   $t5, 132($t1)		# draw doodle
        sw   $t5, 252($t1)		# draw doodle
        sw   $t5, 260($t1)		# draw doodle
        
        lw $t1, platform1     			# paint platform1
        sw $t2, 0($t1)  			# paint platform1
  	sw $t2, 4($t1)  			# paint platform1
  	sw $t2, 8($t1)  			# paint platform1
 	sw $t2, 12($t1)  			# paint platform1
 	sw $t2, 16($t1)  			# paint platform1
 	sw $t2, 20($t1)  			# paint platform1
  	sw $t2, 24($t1)  			# paint platform1
  	sw $t2, 28($t1) 			# paint platform1
  	
  	lw $t1, platform2    			 # paint platform2
        sw $t2, 0($t1) 				 # paint platform2
  	sw $t2, 4($t1)  			 # paint platform2
  	sw $t2, 8($t1) 				 # paint platform2
 	sw $t2, 12($t1)  			 # paint platform2
 	sw $t2, 16($t1)  			# paint platform2
 	sw $t2, 20($t1)  			# paint platform2
  	sw $t2, 24($t1) 			 # paint platform2
  	sw $t2, 28($t1) 			 # paint platform2
  	sw $t2, 28($t1)				# paint platform2
  	
  	lw $t1, platform3    			 # paint platform3
        sw $t2, 0($t1)  			# paint platform3
  	sw $t2, 4($t1)  			# paint platform3
  	sw $t2, 8($t1)  			# paint platform3
 	sw $t2, 12($t1)  			# paint platform3
 	sw $t2, 16($t1)  			# paint platform3
 	sw $t2, 20($t1)  			# paint platform3
  	sw $t2, 24($t1) 			 # paint platform3
  	sw $t2, 28($t1)				# paint platform3
  	sw $t2, 28($t1)				# paint platform3
  	
  	lw $t1, platform4   			  # paint platform4
        sw $t2, 0($t1)  			# paint platform4
  	sw $t2, 4($t1)  			# paint platform4
  	sw $t2, 8($t1)  			# paint platform4
 	sw $t2, 12($t1) 			 # paint platform4
 	sw $t2, 16($t1) 			 # paint platform4
 	sw $t2, 20($t1)  			# paint platform4
  	sw $t2, 24($t1)  			# paint platform4
  	sw $t2, 28($t1)				# paint platform4
  	sw $t2, 28($t1)				# paint platform4
        
# if doodle touch the platform4
        lw   $t1, doodle                   	# load doodle 
        lw   $t9, platform4			# load platform 
        addi $t1, $t1, 384			# find the bottom of the doodle
        addi $t9, $t9, -4
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, BeginJump		# check the bottom of the doodle touch the platform or not
        
# if doodle touch the platform3
        lw   $t1, doodle                	# load doodle
        lw   $t9, platform3			# load platform
        addi $t1, $t1, 384			# find the bottom of the doodle
        addi $t9, $t9, -4
        beq  $t1, $t9, PlatformFall
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        
 # if doodle touch the platform2
        lw   $t1, doodle                 	# load doodle
        lw   $t9, platform2			# load platform
        addi $t1, $t1, 384			# find the bottom of the doodle
        addi $t9, $t9, -4
        beq  $t1, $t9, PlatformFall		
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        addi $t9, $t9, 4			# platform move to next unit 
        beq  $t1, $t9, PlatformFall		# check the bottom of the doodle touch the platform or not
        
# since the doodle is impossible to touch platform1 we do not need to check that situation 


# all these part is to check is touch the bottom or not 
        lw   $t1, doodle
        lw   $t9, displayAddress
        addi $t9, $t9, 4096			# last unit in screen
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        addi $t9, $t9, 4			# move to next unit in the very bottom line
        beq  $t1, $t9, Redraw			# check the doodle check the very bottom or not
        
        
        li   $v0, 32                		#sleep
        li   $a0, 80				#sleep 80ms
        syscall
       
        J NewFall
        
PlatformFall:
        li   $v0, 32               		 # sleep
        li   $a0, 80 				 # sleep 80ms
        syscall
        
        add  $s2, $zero, $zero
        addi $s3, $zero, 8
        
BeginPlatformFall:

        lw   $t8, 0xffff0000
        beq  $t8, 1, KeyboardPlatformFall
        sw   $zero, 0xffff0000
        J PF

# ==================================================================
# ==================================================================
# ==================================================================
# keyboard setting 

KeyboardPlatformFall:
        lw   $t8, 0xffff0004
        beq  $t8, 0x6a, KeyJPlatformFall
        beq  $t8, 0x6b, KeyKPlatformFall
        J PF
        
KeyJPlatformFall:  
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, -4
        sw   $t1, doodle
        
        J PF
KeyKPlatformFall:   
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, 4
        sw   $t1, doodle
        J PF
        
# keyboard setting
# ==================================================================
# ==================================================================
# ==================================================================

PF:        
        lw   $t6, platform1
        lw   $t7, platform2
        lw   $t8, platform3
        lw   $t9, platform4
        
        
        sw $t3, 0($t6)  
  	sw $t3, 4($t6)  
  	sw $t3, 8($t6)  
 	sw $t3, 12($t6)  
 	sw $t3, 16($t6)  
 	sw $t3, 20($t6)  
  	sw $t3, 24($t6)  
  	sw $t3, 28($t6) 
  	lw   $t6, platform1
  	addi $t6, $t6, 128
  	sw   $t6, platform1
  	sw $t2, 0($t6)  
  	sw $t2, 4($t6)  
  	sw $t2, 8($t6)  
 	sw $t2, 12($t6)  
 	sw $t2, 16($t6)  
 	sw $t2, 20($t6)  
  	sw $t2, 24($t6)  
  	sw $t2, 28($t6)
  	
  	sw $t3, 0($t7)  
  	sw $t3, 4($t7)  
  	sw $t3, 8($t7)  
 	sw $t3, 12($t7)  
 	sw $t3, 16($t7)  
 	sw $t3, 20($t7)  
  	sw $t3, 24($t7)  
  	sw $t3, 28($t7) 
  	lw   $t7, platform2
  	addi $t7, $t7, 128
  	sw   $t7, platform2
  	sw $t2, 0($t7)  
  	sw $t2, 4($t7)  
  	sw $t2, 8($t7)  
 	sw $t2, 12($t7)  
 	sw $t2, 16($t7)  
 	sw $t2, 20($t7)  
  	sw $t2, 24($t7)  
  	sw $t2, 28($t7)
  	
  	sw $t3, 0($t8)  
  	sw $t3, 4($t8)  
  	sw $t3, 8($t8)  
 	sw $t3, 12($t8)  
 	sw $t3, 16($t8)  
 	sw $t3, 20($t8)  
  	sw $t3, 24($t8)  
  	sw $t3, 28($t8) 
  	lw   $t8, platform3
  	addi $t8, $t8, 128
  	sw   $t8, platform3
  	sw $t2, 0($t8)  
  	sw $t2, 4($t8)  
  	sw $t2, 8($t8)  
 	sw $t2, 12($t8)  
 	sw $t2, 16($t8)  
 	sw $t2, 20($t8)  
  	sw $t2, 24($t8)  
  	sw $t2, 28($t8)
  	
  	sw $t3, 0($t9)  
  	sw $t3, 4($t9)  
  	sw $t3, 8($t9)  
 	sw $t3, 12($t9)  
 	sw $t3, 16($t9)  
 	sw $t3, 20($t9)  
  	sw $t3, 24($t9)  
  	sw $t3, 28($t9) 
  	lw   $t9, platform4
  	addi $t9, $t9, 128
  	sw   $t9, platform4
  	sw $t2, 0($t9)  
  	sw $t2, 4($t9)  
  	sw $t2, 8($t9)  
 	sw $t2, 12($t9)  
 	sw $t2, 16($t9)  
 	sw $t2, 20($t9)  
  	sw $t2, 24($t9)  
  	sw $t2, 28($t9)
  	
  	lw   $t1, doodle          
  	sw   $t5, 0($t1)
        sw   $t5, 124($t1)
        sw   $t5, 128($t1)
        sw   $t5, 132($t1)
        sw   $t5, 252($t1)
        sw   $t5, 260($t1)
  	
  	li   $v0, 32                #sleep
        li   $a0, 80
        syscall
  	
        addi $s2, $s2, 1
        bne  $s2, $s3, BeginPlatformFall
        
RefreshAllPlatforms:
       lw $t6, platform1    	 		# load platform1
       lw $t7, platform2	 		# load platform2
       lw $t8, platform3			# load platform3
       sw $t6, platform2			# store platform2
       sw $t7, platform3			# store platform3
       sw $t8, platform4			# store platform4
       
       lw $t7, displayAddress  			# set a start address from platform
       	addi $t7, $t7, 768  			# start from the 7th line
       
       li $v0, 42  				# make a x-random
	li $a0, 0  				# ...
	li $a1, 23  				# ...
	syscall   				# ...
  
 	sll $a0, $a0, 2  			# x times 4 from units to bytes 
  	
	add $t7, $t7, $a0 			# update address(plus x)
	sw  $t7, platform1 #

	sw $t2, 0($t7) 				# draw platform
  	sw $t2, 4($t7)  			# ...
  	sw $t2, 8($t7)  			# ...
 	sw $t2, 12($t7)  			# ...
 	sw $t2, 16($t7)  			# ...
 	sw $t2, 20($t7) 		 	# ...
  	sw $t2, 24($t7) 			# ...
  	sw $t2, 28($t7)  			# draw platform
       
               
        
AnotherJump:
        add  $s0, $zero, $zero
        addi $s1, $zero, 4    			# jump 12 as setting, and platform goes up 6, so move 4 units up

# key board setting 
# =========================================================   
# =========================================================      
# =========================================================      
# =========================================================      
   
BeginAnotherJump:

        lw   $t8, 0xffff0000
        beq  $t8, 1, KeyboardAJump
        sw   $zero, 0xffff0000
        J AJump
        
KeyboardAJump:
        lw   $t8, 0xffff0004
        beq  $t8, 0x6a, KeyJAJump
        beq  $t8, 0x6b, KeyKAJump
        J Fall
        
KeyJAJump:  
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, -4
        sw   $t1, doodle
        
        J AJump
        
KeyKAJump:   
        sw   $zero, 0xffff0004
        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        lw   $t1, doodle
        addi $t1, $t1, 4
        sw   $t1, doodle
        J AJump

# =========================================================   
# =========================================================      
# =========================================================      
# =========================================================      
# keybard setting 
          
AJump:        
        lw   $t1, doodle			
        sw   $t3, 0($t1)
        sw   $t3, 124($t1)
        sw   $t3, 128($t1)
        sw   $t3, 132($t1)
        sw   $t3, 252($t1)
        sw   $t3, 260($t1)
        
        addi $t1, $t1, -128
        sw   $t1, doodle
        sw   $t5, 0($t1)
        sw   $t5, 124($t1)
        sw   $t5, 128($t1)
        sw   $t5, 132($t1)
        sw   $t5, 252($t1)
        sw   $t5, 260($t1)
        
        lw $t1, platform1     		# load platform1
        sw $t2, 0($t1)  		# paint platform1
  	sw $t2, 4($t1)  		# paint platform1
  	sw $t2, 8($t1)  		# paint platform1
 	sw $t2, 12($t1)  		# paint platform1
 	sw $t2, 16($t1)  		# paint platform1
 	sw $t2, 20($t1)  		# paint platform1
  	sw $t2, 24($t1)  		# paint platform1
  	sw $t2, 28($t1)  		# paint platform1
  	
  	lw $t1, platform2     		# load platform2
        sw $t2, 0($t1)  		# paint platform2
  	sw $t2, 4($t1)  		# paint platform2
  	sw $t2, 8($t1)  		# paint platform2
 	sw $t2, 12($t1)  		# paint platform2
 	sw $t2, 16($t1)  		# paint platform2
 	sw $t2, 20($t1)  		# paint platform2
  	sw $t2, 24($t1)  		# paint platform2
  	sw $t2, 28($t1)  		# paint platform2
  		
  	lw $t1, platform3     		# load platform3
        sw $t2, 0($t1)  		# paint platform3
  	sw $t2, 4($t1)  		# paint platform3
  	sw $t2, 8($t1)  		# paint platform3
 	sw $t2, 12($t1)  		# paint platform3
 	sw $t2, 16($t1)  		# paint platform3
 	sw $t2, 20($t1)  		# paint platform3
  	sw $t2, 24($t1)  		# paint platform3
  	sw $t2, 28($t1)			# paint platform3
  	
  	lw $t1, platform4    		# load platform4
        sw $t2, 0($t1)  		# paint platform4
  	sw $t2, 4($t1) 			# paint platform4
  	sw $t2, 8($t1)  		# paint platform4
 	sw $t2, 12($t1)  		# paint platform4
 	sw $t2, 16($t1)  		# paint platform4
 	sw $t2, 20($t1)  		# paint platform4
  	sw $t2, 24($t1)  		# paint platform4
  	sw $t2, 28($t1)			# paint platform4
        
        li   $v0, 32               	#sleep
        li   $a0, 80			#sleep 80ms
        syscall
        
        addi $s0, $s0, 1
        beq  $s0, $s1, NewFall
        J BeginAnotherJump

# redraw the screen  
Redraw:
	
	beq $s6, $s7, EndGame   	 # if start == end, exit 
	sw $t3, 0($s7)    		 # load color into register
	addi $s7, $s7, 4    		 # move to next register
	bne $s6, $s7, Redraw   		 # if start != end, back to loop


# this part is to draw a gid doddle when you die the game 
EndGame:
	
	lw $s5, paint
	
	sw $t5, 1076($s5)
	sw $t5, 1080($s5)
	sw $t5, 1084($s5)
	sw $t5, 1088($s5)
	sw $t5, 1092($s5)
	
	sw $t5, 1204($s5)
	sw $t5, 1208($s5)
	sw $t5, 1212($s5)
	sw $t5, 1216($s5)
	sw $t5, 1220($s5)
	
	sw $t5, 1332($s5)
	sw $t5, 1336($s5)
	sw $t5, 1340($s5)
	sw $t5, 1344($s5)
	sw $t5, 1348($s5)
	
	sw $t5, 1460($s5)
	sw $t5, 1464($s5)
	sw $t5, 1468($s5)
	sw $t5, 1472($s5)
	sw $t5, 1476($s5)
	
	sw $t5, 1588($s5)
	sw $t5, 1592($s5)
	sw $t5, 1596($s5)
	sw $t5, 1600($s5)
	sw $t5, 1604($s5)
	
	sw $s7, 1696($s5)
	sw $s7, 1700($s5)
	sw $s7, 1704($s5)
	sw $s7, 1708($s5)
	sw $s7, 1712($s5)
	
	sw $s7, 1716($s5)
	sw $s7, 1720($s5)
	sw $s7, 1724($s5)
	sw $s7, 1728($s5)
	sw $s7, 1732($s5)
	
	sw $s7, 1736($s5)
	sw $s7, 1740($s5)
	sw $s7, 1744($s5)
	sw $s7, 1748($s5)
	sw $s7, 1752($s5)
	
	sw $s7, 1824($s5)
	sw $s7, 1828($s5)
	sw $s7, 1832($s5)
	sw $s7, 1836($s5)
	sw $s7, 1840($s5)
	
	sw $s7, 1844($s5)
	sw $s7, 1848($s5)
	sw $s7, 1852($s5)
	sw $s7, 1856($s5)
	sw $s7, 1860($s5)
	
	sw $s7, 1864($s5)
	sw $s7, 1868($s5)
	sw $s7, 1872($s5)
	sw $s7, 1876($s5)
	sw $s7, 1880($s5)
	
	sw $s7, 1952($s5)
	sw $s7, 1956($s5)
	sw $s7, 1960($s5)
	sw $s7, 1964($s5)
	sw $s7, 1968($s5)
	
	sw $s7, 1972($s5)
	sw $s7, 1976($s5)
	sw $s7, 1980($s5)
	sw $s7, 1984($s5)
	sw $s7, 1988($s5)
	
	sw $s7, 1992($s5)
	sw $s7, 1996($s5)
	sw $s7, 2000($s5)
	sw $s7, 2004($s5)
	sw $s7, 2008($s5)
	
	sw $s7, 2080($s5)
	sw $s7, 2084($s5)
	sw $s7, 2088($s5)
	sw $s7, 2092($s5)
	sw $s7, 2096($s5)
	
	sw $s7, 2100($s5)
	sw $s7, 2104($s5)
	sw $s7, 2108($s5)
	sw $s7, 2112($s5)
	sw $s7, 2116($s5)
	
	sw $s7, 2120($s5)
	sw $s7, 2124($s5)
	sw $s7, 2128($s5)
	sw $s7, 2132($s5)
	sw $s7, 2136($s5)
	
	sw $s7, 2208($s5)
	sw $s7, 2212($s5)
	sw $s7, 2216($s5)
	sw $s7, 2220($s5)
	sw $s7, 2224($s5)
	
	sw $s7, 2228($s5)
	sw $s7, 2232($s5)
	sw $s7, 2236($s5)
	sw $s7, 2240($s5)
	sw $s7, 2244($s5)
	
	sw $s7, 2248($s5)
	sw $s7, 2252($s5)
	sw $s7, 2256($s5)
	sw $s7, 2260($s5)
	sw $s7, 2264($s5)
	
	sw $t2, 2336($s5)
	sw $t2, 2340($s5)
	sw $t2, 2344($s5)
	sw $t2, 2348($s5)
	sw $t2, 2352($s5)
	
	sw $t2, 2376($s5)
	sw $t2, 2380($s5)
	sw $t2, 2384($s5)
	sw $t2, 2388($s5)
	sw $t2, 2392($s5)
	
	sw $t2, 2464($s5)
	sw $t2, 2468($s5)
	sw $t2, 2472($s5)
	sw $t2, 2476($s5)
	sw $t2, 2480($s5)
	
	sw $t2, 2504($s5)
	sw $t2, 2508($s5)
	sw $t2, 2512($s5)
	sw $t2, 2516($s5)
	sw $t2, 2520($s5)
	
	sw $t2, 2592($s5)
	sw $t2, 2596($s5)
	sw $t2, 2600($s5)
	sw $t2, 2604($s5)
	sw $t2, 2608($s5)
	
	sw $t2, 2632($s5)
	sw $t2, 2636($s5)
	sw $t2, 2640($s5)
	sw $t2, 2644($s5)
	sw $t2, 2648($s5)
	
	sw $t2, 2720($s5)
	sw $t2, 2724($s5)
	sw $t2, 2728($s5)
	sw $t2, 2732($s5)
	sw $t2, 2736($s5)
	
	sw $t2, 2760($s5)
	sw $t2, 2764($s5)
	sw $t2, 2768($s5)
	sw $t2, 2772($s5)
	sw $t2, 2776($s5)
	
	sw $t2, 2848($s5)
	sw $t2, 2852($s5)
	sw $t2, 2856($s5)
	sw $t2, 2860($s5)
	sw $t2, 2864($s5)
	
	sw $t2, 2888($s5)
	sw $t2, 2892($s5)
	sw $t2, 2896($s5)
	sw $t2, 2900($s5)
	sw $t2, 2904($s5)
	
                
Restart:
        lw  $t8, 0xffff0000
        beq $t8, 1, KeyboardRestart
        J Restart
       
KeyboardRestart:
        sw  $zero, 0xffff0000
        lw  $t8, 0xffff0004
        beq $t8, 0x68, KeyH
        J Restart
        
# this is the end of the game if you die and you can press to restart game 

KeyH:  
        sw  $zero, 0xffff0004
        J Begin     
        


  
Exit:
  	li $v0, 10
  	syscall
