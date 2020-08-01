import cv2
import numpy as np
import matplotlib.pyplot as plt
import random as rng 

img = cv2.imread("./mall2.png")
r_img = cv2.resize(img,None,fx=2,fy=2,interpolation=cv2.INTER_AREA)

'''
        
def wall_detection(r_img):
    
# line detection

    hsv = cv2.cvtColor(r_img,cv2.COLOR_BGR2HSV)
    lower_black = np.array([0,0,0])
    upper_black = np.array([0,8,8])
    mask = cv2.inRange(hsv,lower_black,upper_black)
    mask = cv2.bitwise_not(mask)
    #mask = np.zeros_like(dilated)
    
    blank = np.ones_like(r_img)*255
    
    walls = cv2.medianBlur(mask,5)
    kernel = np.ones((5,5),np.uint8)
    erosion = cv2.erode(walls, kernel, iterations=1)
    dilated = cv2.dilate(erosion,kernel,iterations = 2)
    
    canny = cv2.Canny(dilated,100,200)
    contours, hierarchy = cv2.findContours(canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    cv2.drawContours(blank, contours, -1, (255,0,0), 3)
    

      
    plt.imshow(img,cmap='gray')
    plt.show()
    
    plt.imshow(hsv)
    plt.show()
    plt.imshow(mask)
    plt.show()
    plt.imshow(walls,cmap='gray')
    plt.show()
    cv2.imshow("dilated",canny)
    plt.show()
    
    cv2.imshow("walls",walls)
    cv2.imshow("blank",blank)
    
    cv2.imwrite("dilated.jpg",dilated)
    cv2.imwrite("mask.jpg",mask)
    cv2.imwrite("walls.png",walls)
    cv2.imwrite("cont.jpg",blank)
    
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
    return walls

def door_detection(r_img):
    gray =cv2.cvtColor(r_img,cv2.COLOR_BGR2GRAY)
    kernel = np.ones((5,5),np.uint8)
    erosion = cv2.erode(r_img, kernel, iterations=3)
    dilated = cv2.dilate(erosion,kernel,iterations = 3)
    blurred = cv2.medianBlur(dilated,5)
    canny = cv2.Canny(blurred,100,200)
    contours,hierarchy=cv2.findContours(canny,cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    contour_list=[]
    for contour in contours:
        approx = cv2.approxPolyDP(contour,0.01*cv2.arcLength(contour,True),True)
        area = cv2.contourArea(contour)
        if ((len(approx) > 12) & (area > 100) ):
            contour_list.append(contour)
            
    cv2.drawContours(r_img, contour_list,  -1, (255,0,0), 2)
    cv2.imshow('dilated',dilated)
    cv2.imshow('Objects Detected',r_img)
    cv2.imwrite('arcs.jpg',r_img)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()

def wall_drawing(corners):
    blank = np.ones_like(r_img)*255
    for i in range(len(corners)-1):
        blank = cv2.line(blank,(corners[i][0],corners[i][1]),(corners[i+1][0],corners[i+1][1]),0,5)
    cv2.imshow("walls",blank)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
'''
def check_arc(roi):
    flag =0
    r_img = cv2.resize(roi,None,fx=2,fy=2,interpolation=cv2.INTER_AREA)
    canny = cv2.Canny(r_img,100,200)
    contours,hierarchy=cv2.findContours(canny,cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    contour_list=[]
    for contour in contours:
        approx = cv2.approxPolyDP(contour,0.001*cv2.arcLength(contour,True),True)
        area = cv2.contourArea(contour)
        if (len(approx) > 10 and area>30):
            cv2.drawContours(r_img, contour,  -1, 100, 4)        
            flag = 1
    #cv2.imshow("walls",roi)
    #.imshow("canny",canny)
    #if cv2.waitKey(0) & 0xff == 27:
    #    cv2.destroyAllWindows()
    return flag,r_img
    
def door_detect(wall,v_points,h_points):
    
    gray =cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    #canny = cv2.Canny(gray,100,200)
    
    for i in range(len(h_points)-1):
        if h_points[i][1] in range(h_points[i+1][1]-1,h_points[i+1][1]+1) :
            if h_points[i+1][0] - h_points[i][0] > 10 and h_points[i+1][0] - h_points[i][0] <40:
                p1 = (h_points[i][0],h_points[i][1])
                p2 = (h_points[i+1][0],h_points[i+1][1])
                if(p1[1]> 45):
                    roi = gray[p1[1]-35:p2[1]+35,p1[0]-5:p2[0]+5]
                else:
                    roi = gray[p1[1]:p2[1]+35,p1[0]-5:p2[0]+5]
                flag,roi2=check_arc(roi)
                if(flag == 1):
                    cv2.line(wall,p1,p2,255,2)
                
                name = "./d/"+str(i)+".png"
                cv2.imwrite(name,roi)
      
    for i in range(len(v_points)-1):
        #range(corners[i+1][0]-1,corners[i+1][0]+1)
        if v_points[i][0] in range(v_points[i+1][0]-1,v_points[i+1][0]+1) :
            if v_points[i+1][1] - v_points[i][1] > 15 and v_points[i+1][1] - v_points[i][1] <55:
                p1 = (v_points[i][0],v_points[i][1])
                p2 = (v_points[i+1][0],v_points[i+1][1])
                if(p1[0]> 45):
                    roi = gray[p1[1]:p2[1],p1[0]-35:p2[0]+35]
                else:
                    roi = gray[p1[1]:p2[1],p1[0]:p2[0]+35]
                flag,roi2=check_arc(roi)
                if(flag == 1):
                    cv2.line(wall,p1,p2,255,2)
               
                name = "./d/v_"+str(i)+".png"
                print(name)
                cv2.imwrite(name,roi)
            
    
    cv2.imshow("walls",roi)
    cv2.imwrite("rooms.png",wall)
    cv2.imshow("walls1",thresh1)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
    return wall
        
def find_corners(image):
    
    gray_img=cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

    kernel = np.ones((5,5),np.uint8)
    gray = cv2.dilate(gray_img,kernel,iterations = 1)

    ret, thresh1 = cv2.threshold(gray,225, 255, cv2.THRESH_BINARY) 
    
    gray_2 = np.float32(gray)
    dst = cv2.cornerHarris(thresh1,2,3,0.04)
    #print(gray)
    dst = cv2.dilate(dst,None)
    ret, dst = cv2.threshold(dst,0.01*dst.max(),255,0)
    dst = np.uint8(dst)

    # find centroids
    ret, labels, stats, centroids = cv2.connectedComponentsWithStats(dst)

    # define the criteria to stop and refine the corners
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 100, 0.001)
    corners = cv2.cornerSubPix(gray,np.float32(centroids),(5,5),(-1,-1),criteria)
    

    res = np.hstack((centroids,corners))
    res = np.int0(res)
    #img[res[:,1],res[:,0]]=[0,0,255]
    #img[res[:,3],res[:,2]] = [0,255,0]
    #img[centroids[:,0],centroids[:,1]] = [0,0,255]
    coords=[]
    for i in res:
        coords.append((i[2],i[3]))
        
    cv2.imwrite('subpixel5.png',img)

    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
    return thresh1,coords

def corner_analysis(v_points,h_points,thresh):
    gray_img=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    blank = np.ones_like(gray_img)*255

    for i in range(len(v_points)-1):
        #range(corners[i+1][0]-1,corners[i+1][0]+1)
        if v_points[i][0] in range(v_points[i+1][0]-1,v_points[i+1][0]+1) :
            cv2.line(blank,v_points[i],v_points[i+1],0,2)
            
    for i in range(len(h_points)-1):
        #range(corners[i+1][0]-1,corners[i+1][0]+1)
        if h_points[i][1] in range(h_points[i+1][1]-1,h_points[i+1][1]+1) :
            cv2.line(blank,h_points[i],h_points[i+1],0,2)        
    
            
    blank = cv2.bitwise_not(blank,mask=None)
    gray_img = cv2.bitwise_not(gray_img,mask=None)
    res1 = cv2.bitwise_and(gray_img,blank,mask=None)
    res1 = cv2.medianBlur(res1,5)
    remaining = blank -res1
    res2 = cv2.bitwise_or(remaining,gray_img,mask=None)
    res = remaining+gray_img
    cv2.imshow("lines",blank)
    cv2.imshow("res",res1)
    cv2.imshow("remain1",res)
    cv2.imwrite("vertical_lines.png",res1)
    cv2.imwrite("door_candidates.png",remaining)
    
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
    rooms = door_detect(res1,v_points,h_points)
    return rooms
    
def corner_circle(vertical_corners):
    gray_img=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    blank = np.ones_like(gray_img)*255
    
    for corner in vertical_corners:
        cv2.circle(blank,corner,1,0,2)
        
    print(blank.shape)
    print(gray_img.shape)
    
    
    
    blank = cv2.bitwise_not(blank,mask=None)
    gray_img = cv2.bitwise_not(gray_img,mask=None)
    res = cv2.bitwise_or(gray_img,blank,mask=None)
    cv2.imshow("walls",blank)
    cv2.imshow("walls2",res)
    cv2.imwrite("points.jpg",blank)
    cv2.imwrite("./d/points_masked.jpg",res)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
    
        
#walls = wall_detection(r_img)

''' contours, _ = cv2.findContours(canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    # Find the convex hull object for each contour
    hull_list = []
    for i in range(len(contours)):
        hull = cv2.convexHull(contours[i])
        hull_list.append(hull)
    # Draw contours + hull results
    drawing = np.zeros((canny.shape[0], canny.shape[1], 3), dtype=np.uint8)
    for i in range(len(contours)):
        color = (rng.randint(0,256), rng.randint(0,256), rng.randint(0,256))
        cv2.drawContours(drawing, contours, i, color)
        cv2.drawContours(drawing, hull_list, i, color)
    # Show in a window
    cv2.imshow('Contours', drawing)
    cv2.imwrite("hulls.png",drawing)
    if cv2.waitKey(0) & 0xff == 27:
        cv2.destroyAllWindows()
'''
def room_detection(rooms):
    rooms = cv2.medianBlur(rooms,5)
    canny = cv2.Canny(rooms,100,200)
    contours,hierarchy = cv2.findContours(canny,cv2.RETR_TREE,
                                          cv2.CHAIN_APPROX_SIMPLE)

    hierarchy = hierarchy[0] 
    hull_list=[]
    # For each contour, find the bounding rectangle and draw it
    for component in zip(contours, hierarchy):
        currentContour = component[0]
        currentHierarchy = component[1]
        x,y,w,h = cv2.boundingRect(currentContour)
        if currentHierarchy[2] < 0:
            hull = cv2.convexHull(currentContour)
            hull_list.append(hull)
                
            # these are the innermost child components
            
            #cv2.rectangle(img,(x,y),(x+w,y+h),(64,224,208),-1)
        #elif currentHierarchy[3] < 0:
            # these are the outermost parent components
            #cv2.rectangle(img,(x,y),(x+w,y+h),(0,255,0),3)
    cv2.drawContours(img,hull_list,-1,(64,224,208),-1)
  
    for hull in hull_list:
        M = cv2.moments(hull)
        if M["m00"] >100:
            X = int(M["m10"] / M["m00"])
            Y = int(M["m01"] / M["m00"])
        else:
            continue
        cv2.circle(img, (X, Y), 4, (0, 0, 255), -1)
        cv2.putText(img, "center", (X - 20, Y - 20),cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)
   
    # Finally show the image
    cv2.imshow('img',img)
    cv2.imwrite('room_boundaries.png',img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
 
    
#walls=wall_detection(img)
thresh1,corners = find_corners(img)


horizontal_points = corners.copy()
vertical_points = corners.copy()
vertical_points.sort(key= lambda x:x[0])
horizontal_points.sort(key= lambda x:x[1])
print(vertical_points)
print()
corner_circle(corners)
rooms=corner_analysis(vertical_points,horizontal_points,thresh1)
room_detection(rooms)
#wall_drawing(corners)


kernel = np.ones((5,5),np.uint8)
thick_walls = cv2.erode(thresh1, kernel, iterations=2) #thick walls

cv2.imshow("erosion",thick_walls)
cv2.waitKey(0)
cv2.destroyAllWindows()