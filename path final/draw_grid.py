import cv2
import numpy as np
import math


def check_white(block,thresh1,a,b,l):
        h,w=block.shape
        count=0
        for i in range(b,b+l):
            for j in range(a,a+l):
                if(i<thresh1.shape[0] and j<thresh1.shape[1]):
                    if(block[i][j]>0):
                        count=count+1
        if(count>1):
            return True
        return False


def Form_Grid(img,l):
    _,thresh1 = cv2.threshold(img,230,255,cv2.THRESH_BINARY)
    thresh1 = cv2.bitwise_not(thresh1)
    w,h=thresh1.shape[1],thresh1.shape[0]
    
    rows= math.ceil(h/l)
    columns= math.ceil(w/l)
    grid = np.zeros(shape=(rows,columns))
    for i in range(0,w,l):
        for j in range(0,h,l):
            if(check_white(thresh1,thresh1,i,j,l)):
                grid[int(j/l)][int(i/l)]=1
    return grid


        
