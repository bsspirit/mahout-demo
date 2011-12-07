#/usr/bin/python

def last_lines(filename, lines = 1):
    #print the last several line(s) of a text file
    """
    Argument filename is the name of the file to print.
    Argument lines is the number of lines to print from last.
    """
    block_size = 1024
    block = ''
    nl_count = 0
    start = 0
    fsock = file(filename, 'rU')
    try:
        #seek to end
        fsock.seek(0, 2)
        #get seek position
        curpos = fsock.tell()
        while(curpos > 0): #while not BOF
            #seek ahead block_size+the length of last read block
            curpos -= (block_size + len(block));
            if curpos < 0: curpos = 0
            fsock.seek(curpos)
            #read to end
            block = fsock.read()
            nl_count = block.count('\n')
            #if read enough(more)
            if nl_count >= lines: break
        #get the exact start position
        for n in range(nl_count-lines+1):
            start = block.find('\n', start)+1
    finally:
        fsock.close()
    #print it out
    print block[start:]

#python ../../python/LessData.py ratings.dat 50000 50000 > ratings_u50000.dat 
#python ../../python/LessData.py gender.dat 50000 > gender_u50000.dat
def less_lines(f, userid=100, itemid=-1, value=-1):
    for line in open(f,'r').readlines():
        if len(line)<2:continue
        l=line.split(',')
        if int(l[0])>int(userid): continue 
        if int(itemid)!=-1 and int(l[1])>int(itemid): continue
        if float(value) !=-1 and float(l[2])>float(value): continue  
        print line[:-1]

if __name__ == '__main__':
    import sys
    #f = "../data/ch02.csv"
    #f="../data/libimseti/ratings.dat"
    #last_lines(f, 5) #print the last 5 lines of THIS file
    #print "arg[0] file path"
    #print "arg[1] user max value (int)"
    #print "arg[2] item max value (int)"
    #print "arg[3] preference max value (float)"

    if len(sys.argv) == 3:less_lines(sys.argv[1],sys.argv[2])
    if len(sys.argv) == 4:less_lines(sys.argv[1],sys.argv[2],sys.argv[3])
