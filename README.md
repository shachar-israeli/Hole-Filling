# Hole-Filling
The goal is to build a small image processing library that fills holes in images, along with a small command line utility that uses that library, and answer a few questions.


#Hole Filling Algorithm#

Shachar Israeli
30/07/2020

**Submitted Files**
#src: ArgsProccess.java, Constants.java, ImageProcces.java, Main.java, Point.java, FillingHole.java
#Image: simulated images to test on.

** environment **
OpenCV need to be installed.

** install & load **
To build and run the program (example with args):
cd src
javac -cp path/to/jar *.java
java "-Djava.library.path=path/to/dll/lib" -classpath "path/to/src ;path/to/opencv-430.jar" Main images/red.jpg images/red_mask.jpg 2 0.001 8

** Usage **
[image path] [mask path] [z] [epsilon] [pixel connectivity: 4/8]
*Change the flag of ArgsProccess to true, in order to get a default args.

** Output **
output image will be created under the same path as the input folder.

** Algorithem steps **
1. Convert the source image to a grayscale format, Assign -1 value to every pixel where defined as hole in the mask.
2. scale the color values to [0.0-1.0]
*Without using the steps above*
3. Search the hole(one of the pixels that defined the hole)
5. Applies the filling algorithm for every pixel in the hole, using a queue to progress on the hole pixels.
7. Save the filled image.

**Implementation notes**
###################

* the hole is defined with white color on the mask image. I used a threshold of 245.
* I assumed that every image has only a single hole and it is continuous. 
I used this information to get all the pixels of the hole with the "BFS algorithm" way. I tried to use it in order to not iterate over the whole image if I already know where the hole is.
* I implemented the approximates approach that described in Q2.

##* Theoretical questions *##

** 1 **
Assume the hole and the boundary are already found, the algorithm goes through all pixels in the hole (n) and for each pixel in the hole
calculates the missing value based on all pixels in the boundaries (m) =>O(n*m).
For a generic shape of hole, each hole pixel can adds up to 8 boundary pixels =>  n * m < O(n^2).

** 2 ** 

An algorithm that can approximate the result in o(n) is an algorithm that
instead of running over all the boundary points for calculating the value of a pixel in the hole, use only the value of its neighboring pixels only.
the idea is to take only the most significant pixels for the wight function( the neighbors of the missing pixel have less distance from him and then get more significant to the final value).
after every missing pixel detection, calculate the new value and use it on the missing neighbors (start from outside to inside)
 (I've implemented the approximates approach)
