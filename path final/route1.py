import cv2
import numpy as np
import math
import matplotlib.pyplot as plt
import heapq
from draw_grid import *
class solver:
    def __init__(self):
        pass
        
    def hue(self,a,b):
        return np.sqrt((a[0]-b[0])**2 + (a[1]-b[1])**2)

    def astar(self,start,end,grid):
        child=[(0,1),(0,-1),(1,0),(-1,0),(1,1),(1,-1),(-1,1),(-1,-1)]
        heap=[]
        closed_list=set()
        gscore={start:0}
        fscore={start:self.hue(start,end)}
        parent={}
        heapq.heappush(heap,(fscore[start],start))
        while heap:
            current=heapq.heappop(heap)[1]
            closed_list.add(current)
            if(current==end):
                data=[]
                while current in parent:
                    data.append(current)
                    current=parent[current]
                return data

            for i,j in child:
                sucessor=current[0]+i,current[1]+j
                hscore=gscore[current]+self.hue(current,sucessor)
                if 0<=sucessor[0]<grid.shape[0]:
                    if 0<=sucessor[1]<grid.shape[1]:
                        if grid[sucessor[0]][sucessor[1]]:
                            continue
                    else:
                        continue
                else:
                    continue
                if sucessor in closed_list and hscore>=gscore.get(sucessor,0):
                    continue
                if hscore<gscore.get(sucessor,0) or sucessor not in [i[1] for i in heap]:
                    gscore[sucessor]=hscore
                    parent[sucessor]=current
                    fscore[sucessor]=hscore+self.hue(sucessor,end)
                    heapq.heappush(heap,(fscore[sucessor],sucessor))

        return False



def line(route):
    xc=[]
    yc=[]
    for i in (range(0,len(route))):
        x=route[i][0]
        y=route[i][1]
        xc.append(x)
        yc.append(y)
    return xc,yc

def route(img,grid,start_x,start_y,end_x,end_y,l):
    
    #thresh1 = img
    #_,thresh1 = cv2.threshold(img,230,255,cv2.THRESH_BINARY)
    #thresh1 = cv2.bitwise_not(thresh1)
    #w,h=thresh1.shape[1],thresh1.shape[0]
    #l = 2
    #rows= math.ceil(h/l)
    #columns= math.ceil(w/l)
    start_x=math.ceil(start_x/l)
    start_y=math.ceil(start_y/l)
    end_x=math.ceil(end_x/l)
    end_y=math.ceil(end_y/l)
    start=(start_x,start_y)
    end=(end_x,end_y)
    
    solve=solver()
    #grid=Form_Grid(thresh1,w,h,l,rows,columns)
    
    route=solve.astar(start,end,grid)
    if(route==False):
        print("No path")
    else:
        route+=[start]
        route=route[::-1]
        xc,yc=line(route)
        fig,ax=plt.subplots()
        ax.imshow(grid,cmap=plt.cm.Spectral)
        ax.plot(yc,xc,color="black")
        ax.scatter(start[1],start[0])
        ax.scatter(end[1],end[0])
        plt.show()
        print(xc)
        print(yc)
        waypoints = []
        for x,y in zip(xc,yc):
            x = x*l
            y= y*l
            waypoints.append((x,y))
    #print(waypoints)
        return waypoints  

if ( __name__ =="__main__"):
    img = cv2.imread("walls.jpg",0)
    waypoints=route(img,278,113,116,346)
    
    for i in range(len(waypoints)-1):
        point1=(waypoints[i][1],waypoints[i][0])
        point2=(waypoints[i+1][1],waypoints[i+1][0])
        cv2.line(img,point1,point2,100,2)
    cv2.imshow("img",img)
    cv2.imwrite("route.jpg",img)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
