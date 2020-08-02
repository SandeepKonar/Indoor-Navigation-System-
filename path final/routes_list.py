import cv2
import numpy as np
from route1 import *
starting = (200,560)
cnt = 31
p1 = starting
routes_list=[] 

img = cv2.imread("walls.jpg",0)
rooms = [['room 1', (231, 692)], ['room 2', (165, 692)], ['room 3', (231, 645)],
          ['room 4', (165, 645)], ['room 5', (231, 594)], ['room 6', (165, 541)],
          ['room 7', (446, 537)], ['room 8', (446, 630)], ['room 9', (390, 630)],
          ['room 10', (335, 630)], ['room 11', (362, 537)],
          ['room 12', (196, 432)], ['room 13', (363, 385)], 
          ['room 14', (447, 292)], ['room 15', (363, 292)], 
          ['room 16', (190, 274)], ['room 17', (280, 813)],
          ['room 18', (81, 798)], ['room 19', (546, 745)],
          ['room 20', (56, 682)], ['room 21', (545, 671)],
          ['room 22', (56, 594)], ['room 23', (56, 499)],
          ['room 24', (545, 453)], ['room 25', (56, 402)],
          ['room 26', (545, 302)], ['room 27', (56, 303)],
          ['room 28', (56, 205)], ['room 29', (416, 141)], 
          ['room 30', (266, 118)], ['room 31', (85, 57)]]

l = 2

grid = Form_Grid(img,l)
for r in rooms:
    p2 = r[1]
    waypoints = route(img,grid,p1[0],p1[1],p2[1],p2[0],l)
    try:
        for i in range(len(waypoints)-1):
            point1=(waypoints[i][1],waypoints[i][0])
            point2=(waypoints[i+1][1],waypoints[i+1][0])
            cv2.line(img,point1,point2,100,2)
            
    except:
        print("no path")
        
    
    