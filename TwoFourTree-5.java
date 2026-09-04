public class TwoFourTree {
    public class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            return this.values == 1;
        }

        public boolean isThreeNode() {
        	return this.values == 2;
        }

        public boolean isFourNode() {
        	return this.values == 3;
        }

        public boolean isRoot() {
            return this.parent == null;
        }

        
        //constructors
        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
            this.values = 1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            this.values = 2;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.values = 3;
        }


        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;
    
    private TwoFourTreeItem splitNode(TwoFourTreeItem node, int val){
    	//takes in node and value, returns it if it isnt a 4 node. splits into 2 nodes and fixes pointer, if parent becomes a 4 node, uses value to not backtrack.
    	//cannot take in a null node
        if (!node.isFourNode()) return node;
        
        //System.out.print("\nin split node");
        
        int middleval=node.value2;
        
        TwoFourTreeItem newleftnode= new TwoFourTreeItem(node.value1);
        TwoFourTreeItem newrightnode= new TwoFourTreeItem(node.value3);
        
        
        
        if(!node.isLeaf){
            
            newleftnode.leftChild=node.leftChild;
            newleftnode.leftChild.parent=newleftnode;
            newleftnode.rightChild=node.centerLeftChild;
            newleftnode.rightChild.parent=newleftnode;
            newleftnode.isLeaf=false;
            
            newrightnode.leftChild=node.centerRightChild;
            newrightnode.rightChild=node.rightChild;
            newrightnode.leftChild.parent=newrightnode;
            newrightnode.rightChild.parent=newrightnode;
            newrightnode.isLeaf=false;
            
            
        }
        
        
        if(node.isRoot()){
            TwoFourTreeItem newroot= new TwoFourTreeItem(middleval);
            newroot.rightChild=newrightnode;
            newrightnode.parent=newroot;
            newroot.leftChild=newleftnode;
            newleftnode.parent=newroot;
            newroot.isLeaf=false;
            root=newroot;
            
        }
        else{
            
            
            if(node.parent.isTwoNode()){
                
                if(middleval<node.parent.value1){
                    node.parent.value2=node.parent.value1;
                    node.parent.value1=middleval;
                    
                    node.parent.centerChild=newrightnode;
                    node.parent.leftChild=newleftnode;
                    
                }
                else{
                    node.parent.value2=middleval;
                    
                    node.parent.centerChild=newleftnode;
                    node.parent.rightChild=newrightnode;
                }
            }
            else if(node.parent.isThreeNode()){
                if(middleval<node.parent.value1){
                    node.parent.value3=node.parent.value2;
                    node.parent.value2=node.parent.value1;
                    node.parent.value1=middleval;
                    
                    node.parent.leftChild=newleftnode;
                    node.parent.centerLeftChild=newrightnode;
                    node.parent.centerRightChild=node.parent.centerChild;
                    node.parent.centerChild=null;
                }
                else if(middleval<node.parent.value2){
                    node.parent.value3=node.parent.value2;
                    node.parent.value2=middleval;
                    
                    node.parent.centerLeftChild=newleftnode;
                    node.parent.centerRightChild=newrightnode;
                    node.parent.centerChild=null;
                }
                else{
                    node.parent.value3=middleval;
                    
                    node.parent.rightChild=newrightnode;
                    node.parent.centerRightChild=newleftnode;
                    node.parent.centerLeftChild=node.parent.centerChild;
                    node.parent.centerChild=null;
                }
            }
            node.parent.values++;
            newrightnode.parent=node.parent;
            newleftnode.parent=node.parent;
        }
        
        TwoFourTreeItem parent=newleftnode.parent;
        releasenode(node);
        
        if(parent.isFourNode()){
            if(val<parent.value1){
                return parent.leftChild;
            }
            else if(val<parent.value2){
                return parent.centerLeftChild;
            }
            else if(val< parent.value3){
                return parent.centerRightChild;
            }
            else{
                return parent.rightChild;
            }
        }
        else{
            return parent;
        }
    }


    public boolean addValue(int value) {
    // takes in a value and checks if it exists, if it does it returns 0 if it does not it returns 1. must be an integer and not null
    	
    	//checks if root is null, if it is creates node and adds new value and returns 1
    	if (root==null) {
    		root=  new  TwoFourTreeItem(value);
    		return true;
    	    
    	}
    	
    	//checks if value exists if it does returns 0 else it adds value and returns 1
    	if(!hasValue(value)) {
	    	TwoFourTreeItem tempnode= root;
	    	
	    	while(tempnode!=null){
    	    	//is a four node
    	    	if (tempnode.isFourNode()) {
    	    	    tempnode= splitNode(tempnode,value);
    	    		
    	    		}
	    		
    	    	//case isnt a 4 node and isnt a leaf...keep going!
    	    	else if (!tempnode.isLeaf){
    	    	    if(value<tempnode.value1) {
    	    				tempnode=tempnode.leftChild;
    	    			}
    	    		else if(tempnode.isFourNode() &&value<tempnode.value2){
    	    		    tempnode=tempnode.centerLeftChild;
    	    		    
    	    		}
    	    		else if(tempnode.isThreeNode()&&value<tempnode.value2){
    	    		    tempnode=tempnode.centerChild;
    	    		}
    	    		else if(tempnode.isFourNode() && value<tempnode.value3){
    	    		    tempnode=tempnode.centerRightChild;
    	    		}
    	    		else {
    	    		    tempnode=tempnode.rightChild;
    	    		}
    	    		
    	    	    
    	    	}
    	    	
    	    	//is a leaf node!!
    	    	else {
    	    	    shiftadd(tempnode,value);
    	    	    return true;
    	    	}
	    	}
	    	
    	}
        return false;
     
 }
 
    public boolean hasValue(int value) {
    	//checks tree for a value, returns true if it does and false if it doesnt.if value is 0, there will be problem since numbers are initialized to 0
        if (root == null) {
            return false;
        }

        TwoFourTreeItem current = root;

        while (current != null) {
            // Direct match
            if (value == current.value1 || value == current.value2 || value == current.value3) {
                return true;
            }

            // Traverse children
            if (value < current.value1) {
                current = current.leftChild;
            } 
            else if (current.isTwoNode()) {
                current = current.rightChild;
            } 
            else if (current.isThreeNode()) {
                if (value < current.value2) {
                    current = current.centerChild;
                } 
                else {
                    current = current.rightChild;
                }
            } 
            else if (current.isFourNode()) {
                if (value < current.value2) {
                    current = current.centerLeftChild;
                } else if (value < current.value3) {
                    current = current.centerRightChild;
                } else {
                    current = current.rightChild;
                }
            } 
            
        }

        return false;
    }

    public TwoFourTreeItem releasenode(TwoFourTreeItem node) {
    	//sets node values to 0 to be released by garbage collection
    if (node == null) return null;
    
    node.values = 0;
    node.value1 = node.value2 = node.value3 = 0;
    node.isLeaf = true;
    
    node.parent = null;
    node.leftChild = null;
    node.centerChild = null;
    node.rightChild = null;
    node.centerLeftChild = null;
    node.centerRightChild = null;

    return null;
}

    private String canBorrowFromLeft(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//checks to see if it is possible to borrow from left, if it isnt it returns F or fail, if it is it returns the initial of what child it is to the parent.
        //cannot take in null pointer
    	if (parent.leftChild == child) return "F";
        
        //System.out.print("\nin canborrowlleft");
        
        if(parent.isFourNode()){
            if(parent.centerLeftChild==child && !parent.leftChild.isTwoNode()) return "CL";
            if(parent.centerRightChild==child && !parent.centerLeftChild.isTwoNode()) return "CR";
            if(parent.rightChild==child && !parent.centerRightChild.isTwoNode()) return "R";
        }
        else if(parent.isThreeNode()){
            if(parent.centerChild==child && !parent.leftChild.isTwoNode()) return "C";
            if(parent.rightChild==child && !parent.centerChild.isTwoNode()) return "R";
        }
        else if(parent.isTwoNode()){
           System.out.printf("parent is two node... something wrong"); 
        } 
        
        return "F";
    }
    
    private String canBorrowFromRight(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//checks to see if it is possible to borrow from right, if it isnt it returns F or fail, if it is it returns the initial of what child it is to the parent.
        //cannot take in null pointer
        if (parent.rightChild == child) return "F";
        
       
        if(parent.isFourNode()){
            if(parent.centerRightChild==child && !parent.rightChild.isTwoNode()) return "CR";
            if(parent.centerLeftChild==child && !parent.centerRightChild.isTwoNode()) return "CL";
            if(parent.leftChild==child && !parent.centerLeftChild.isTwoNode()) return "L";
        }
        else if(parent.isThreeNode()){
            if(parent.centerChild==child && !parent.rightChild.isTwoNode()) return "C";
            if(parent.leftChild==child && !parent.centerChild.isTwoNode()) return "L";
        }
        else if(parent.isTwoNode()){
           System.out.printf("\nparent is two node... something wrong"); 
        } 
        
        return "F";
    }
    
    private TwoFourTreeItem borrowFromsibling(TwoFourTreeItem child, TwoFourTreeItem parent) {
        //child should always be a 2node turning into a 3 node
    	//checks to see if it can borrow from right fist and if not then it borrows from left
    	
    	//System.out.print("\nin borrow");
    	
        String point=canBorrowFromRight(child,parent);
        if(!point.equals("F")){
            
            if(!child.isLeaf){
                child.centerChild=child.rightChild;
            }
            
            if(point.equals("L")){
                if(parent.isTwoNode()){//take from rightChild
                    if(!child.isLeaf){
                        child.rightChild=parent.rightChild.leftChild;
                        child.rightChild.parent=child;
                        if(parent.rightChild.isThreeNode()){
                            parent.rightChild.leftChild=parent.rightChild.centerChild;
                            parent.rightChild.centerChild=null;
                        }
                        else if(parent.rightChild.isFourNode()){
                            parent.rightChild.leftChild=parent.rightChild.centerLeftChild;
                            parent.rightChild.centerChild=parent.rightChild.centerRightChild;
                            parent.rightChild.centerRightChild=null;
                            parent.rightChild.centerLeftChild=null;
                        }
                    }
                    
                    
                    shiftadd(child,parent.value1);
                    shiftdel(parent,parent.value1);
                    shiftadd(parent,parent.rightChild.value1);
                    shiftdel(parent.rightChild,parent.rightChild.value1);
                    
                    
                    
                    
                    return parent;
                }
                else if(parent.isThreeNode()){//take from centerChild
                    if(!child.isLeaf){
                        child.rightChild=parent.centerChild.leftChild;
                        child.rightChild.parent=child;
                        if(parent.centerChild.isThreeNode()){
                            parent.centerChild.leftChild=parent.centerChild.centerChild;
                            parent.centerChild.centerChild=null;
                        }
                        else if(parent.centerChild.isFourNode()){
                            parent.centerChild.leftChild=parent.centerChild.centerLeftChild;
                            parent.centerChild.centerChild=parent.centerChild.centerRightChild;
                            parent.centerChild.centerRightChild=null;
                            parent.centerChild.centerLeftChild=null;
                        }
                    }
                    
                    
                    shiftadd(child,parent.value1);
                    shiftdel(parent,parent.value1);
                    shiftadd(parent,parent.centerChild.value1);
                    shiftdel(parent.centerChild,parent.centerChild.value1);
                    
                    
                    
                    return child.parent;
                }
                else if(parent.isFourNode()){//take from centerleftChild
                    if(!child.isLeaf){
                        child.rightChild=parent.centerLeftChild.leftChild;
                        child.rightChild.parent=child;
                        if(parent.centerLeftChild.isThreeNode()){
                            parent.centerLeftChild.leftChild=parent.centerLeftChild.centerChild;
                            parent.centerLeftChild.centerChild=null;
                        }
                        else if(parent.centerLeftChild.isFourNode()){
                            parent.centerLeftChild.leftChild=parent.centerLeftChild.centerLeftChild;
                            parent.centerLeftChild.centerChild=parent.centerLeftChild.centerRightChild;
                            parent.centerLeftChild.centerRightChild=null;
                            parent.centerLeftChild.centerLeftChild=null;
                        }
                        
                    }
                    
                    shiftadd(child,parent.value1);
                    shiftdel(parent,parent.value1);
                    shiftadd(parent,parent.centerLeftChild.value1);
                    shiftdel(parent.centerLeftChild,parent.centerLeftChild.value1);
                    
                    
                    return parent;
                
                    
                }
            }
            else if(point.equals("CR")){
                
                if(!child.isLeaf){
                    child.rightChild=parent.rightChild.leftChild;
                    child.rightChild.parent=child;
                    if(parent.rightChild.isThreeNode()){
                        parent.rightChild.leftChild=parent.rightChild.centerChild;
                        parent.rightChild.centerChild=null;
                    }
                    else if(parent.rightChild.isFourNode()){
                        parent.rightChild.leftChild=parent.rightChild.centerLeftChild;
                        parent.rightChild.centerChild=parent.rightChild.centerRightChild;
                        parent.rightChild.centerRightChild=null;
                        parent.rightChild.centerLeftChild=null;
                    }
                }
                
                
                shiftadd(child,parent.value3);
                shiftdel(parent,parent.value3);
                
                shiftadd(parent,parent.rightChild.value1);
                shiftdel(parent.rightChild,parent.rightChild.value1);
                
                
                
                return parent;
                
                
            }
            else if(point.equals("C")){
                if(parent.isThreeNode()){//take from rightChild
                    if(!child.isLeaf){
                        child.rightChild=parent.rightChild.leftChild;
                        child.rightChild.parent=child;
                        if(parent.rightChild.isThreeNode()){
                            parent.rightChild.leftChild=parent.rightChild.centerChild;
                            parent.rightChild.centerChild=null;
                        }
                        else if(parent.rightChild.isFourNode()){
                            parent.rightChild.leftChild=parent.rightChild.centerLeftChild;
                            parent.rightChild.centerChild=parent.rightChild.centerRightChild;
                            parent.rightChild.centerRightChild=null;
                            parent.rightChild.centerLeftChild=null;
                    }
                    
                    }
                    shiftadd(child,parent.value2);
                    shiftdel(parent,parent.value2);
                    shiftadd(parent,parent.rightChild.value1);
                    shiftdel(parent.rightChild,parent.rightChild.value1);
                    
                    return parent;
                }
            }
            else if(point.equals("CL")){
                
            //take from centerrightChild
                if(!child.isLeaf){
                    child.rightChild=parent.centerRightChild.leftChild;
                    child.rightChild.parent=child;
                    if(parent.centerRightChild.isThreeNode()){
                        parent.centerRightChild.leftChild=parent.centerRightChild.centerChild;
                        parent.centerRightChild.centerChild=null;
                    }
                    else if(parent.centerRightChild.isFourNode()){
                        parent.centerRightChild.leftChild=parent.centerRightChild.centerLeftChild;
                        parent.centerRightChild.centerChild=parent.centerRightChild.centerRightChild;
                        parent.centerRightChild.centerRightChild=null;
                        parent.centerRightChild.centerLeftChild=null;
                    }
                }
                
                
                shiftadd(child,parent.value2);
                shiftdel(parent,parent.value2);
                shiftadd(parent,parent.centerRightChild.value1);
                shiftdel(parent.centerRightChild,parent.centerRightChild.value1);
                
                
                
                return parent;
                
            }
            
        }
        else{
            point=canBorrowFromLeft(child,parent);
            if(!point.equals("F")){
                //move pointer over
                if(!child.isLeaf){
                    child.centerChild=child.leftChild;
                }
            
                if(point.equals("R")){
                    if(parent.isThreeNode()){//take from centerChild
                        if(!child.isLeaf){
                            child.leftChild=parent.centerChild.rightChild;
                            child.leftChild.parent=child;
                        }
                      
                        if(parent.centerChild.isThreeNode()){
                        	if(!child.isLeaf){
	                        	parent.centerChild.rightChild=parent.centerChild.centerChild;
	                            parent.centerChild.centerChild=null;
                        	}
                        	shiftadd(child,parent.value2);
                        	shiftdel(parent,parent.value2);
                            shiftadd(parent,parent.centerChild.value2);
                            shiftdel(parent.centerChild,parent.centerChild.value2);
                            
                        }
                        else if(parent.centerChild.isFourNode()){
                        	if(!child.isLeaf){
	                        	parent.centerChild.rightChild=parent.centerChild.centerRightChild;
	                            parent.centerChild.centerChild=parent.centerChild.centerLeftChild;
	                            parent.centerChild.centerRightChild=null;
	                            parent.centerChild.centerLeftChild=null;
                        	}
                            shiftadd(child,parent.value2);
                            shiftdel(parent,parent.value2);
                            shiftadd(parent,parent.centerChild.value3);
                            shiftdel(parent.centerChild,parent.centerChild.value3);
                            
                        }
                        
                        return parent;
                    }
                    else if(parent.isFourNode()){//take from centerrightChild
                        if(!child.isLeaf){
                            child.leftChild=parent.centerRightChild.rightChild;
                            child.leftChild.parent=child;
                        }
                      
                        if(parent.centerRightChild.isThreeNode()){
                        	if(!child.isLeaf){
	                        	parent.centerRightChild.rightChild=parent.centerRightChild.centerChild;
	                            parent.centerRightChild.centerChild=null;
                        	}
                        	shiftadd(child,parent.value3);
                            shiftdel(parent,parent.value3);
                            shiftadd(parent,parent.centerRightChild.value2);
                            shiftdel(parent.centerRightChild,parent.centerRightChild.value2);
                            
                        }
                        else if(parent.centerRightChild.isFourNode()){
                        	if(!child.isLeaf){
	                        	parent.centerRightChild.rightChild=parent.centerRightChild.centerRightChild;
	                            parent.centerRightChild.centerChild=parent.centerRightChild.centerLeftChild;
	                            parent.centerRightChild.centerRightChild=null;
	                            parent.centerRightChild.centerLeftChild=null;
                        	}
                        	shiftadd(child,parent.value3);
                            shiftdel(parent,parent.value3);
                            shiftadd(parent,parent.centerRightChild.value3);
                            shiftdel(parent.centerRightChild,parent.centerRightChild.value3);
                            
                        }
                        
                        return parent;
                    
                        
                    }
                }
                else if(point.equals("CR")){

                    if(!child.isLeaf){
                        child.leftChild=parent.centerLeftChild.rightChild;
                        child.leftChild.parent=child;
                    }
                    
                    if(parent.centerLeftChild.isThreeNode()){
                    	if(!child.isLeaf){
                    	parent.centerLeftChild.rightChild=parent.centerLeftChild.centerChild;
                        parent.centerLeftChild.centerChild=null;
                    	}
                    	shiftadd(child,parent.value2);
                        shiftdel(parent,parent.value2);
                        shiftadd(parent,parent.centerLeftChild.value2);
                        shiftdel(parent.centerLeftChild,parent.centerLeftChild.value2);
                        
                    }
                    else if(parent.centerLeftChild.isFourNode()){
                    	if(!child.isLeaf){
                    	 parent.centerLeftChild.rightChild=parent.centerLeftChild.centerRightChild;
                         parent.centerLeftChild.centerChild=parent.centerLeftChild.centerLeftChild;
                         parent.centerLeftChild.centerRightChild=null;
                         parent.centerLeftChild.centerLeftChild=null;
                    	}
                    	shiftadd(child,parent.value2);
                        shiftdel(parent,parent.value2);
                        shiftadd(parent,parent.centerLeftChild.value3);
                        shiftdel(parent.centerLeftChild,parent.centerLeftChild.value3);
                       
                    }
                    
                    return parent;
                
                    
                }
                else if(point.equals("C")){
                    
                    if(!child.isLeaf){
                        child.leftChild=parent.leftChild.rightChild;
                        child.leftChild.parent=child;
                    }
                    
                    if(parent.leftChild.isThreeNode()){
                    	if(!child.isLeaf){
                    	parent.leftChild.rightChild=parent.leftChild.centerChild;
                        parent.leftChild.centerChild=null;
                    	}
                    	shiftadd(child,parent.value1);
                        shiftdel(parent,parent.value1);
                        shiftadd(parent,parent.leftChild.value2);
                        shiftdel(parent.leftChild,parent.leftChild.value2);
                        
                    }
                    else if(parent.leftChild.isFourNode()){
                    	if(!child.isLeaf){
                    	parent.leftChild.rightChild=parent.leftChild.centerRightChild;
                        parent.leftChild.centerChild=parent.leftChild.centerLeftChild;
                        parent.leftChild.centerRightChild=null;
                        parent.leftChild.centerLeftChild=null;
                    	}
                    	shiftadd(child,parent.value1);
                        shiftdel(parent,parent.value1);
                        shiftadd(parent,parent.leftChild.value3);
                        shiftdel(parent.leftChild,parent.leftChild.value3);
                        
                    }
                    
                    return parent;
                
                }
                else if(point.equals("CL")){
                    
                    if(!child.isLeaf){
                        child.leftChild=parent.leftChild.rightChild;
                        child.leftChild.parent=child;
                    }
                    
                    if(parent.leftChild.isThreeNode()){
                    	if(!child.isLeaf){
                    	parent.leftChild.rightChild=parent.leftChild.centerChild;
                        parent.leftChild.centerChild=null;
                    	}
                    	
                    	shiftadd(child,parent.value1);
                        shiftdel(parent,parent.value1);
                        shiftadd(parent,parent.leftChild.value2);
                        shiftdel(parent.leftChild,parent.leftChild.value2);
                        
                    }
                    else if(parent.leftChild.isFourNode()){
                    	if(!child.isLeaf){
                    	parent.leftChild.rightChild=parent.leftChild.centerRightChild;
                        parent.leftChild.centerChild=parent.leftChild.centerLeftChild;
                        parent.leftChild.centerRightChild=null;
                        parent.leftChild.centerLeftChild=null;
                    	}
                    	shiftadd(child,parent.value1);
                        shiftdel(parent,parent.value1);
                        shiftadd(parent,parent.leftChild.value3);
                        shiftdel(parent.leftChild,parent.leftChild.value3);
                        
                    }
                    
                    return parent;
                
                
                    
                }
                
                
            }
        }
        
        return parent;
    }
    
    private boolean canMergeWithRight(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//checks to see if a merge is possible with right node, cannot be null
        if (parent.isTwoNode()) {
            return parent.leftChild == child && parent.rightChild.isTwoNode();
        } 
        else if (parent.isThreeNode()) {
            if (parent.leftChild == child) {
                return parent.centerChild.isTwoNode();
            } else if (parent.centerChild == child) {
                return parent.rightChild.isTwoNode();
            }
        } 
        else if (parent.isFourNode()) {
            if (parent.leftChild == child) {
                return parent.centerLeftChild.isTwoNode();
            } else if (parent.centerLeftChild == child) {
                return parent.centerRightChild.isTwoNode();
            } else if (parent.centerRightChild == child) {
                return parent.rightChild.isTwoNode();
            }
        }
        return false;
    }

    private boolean canMergeWithLeft(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//checks to see if a merge is possible with right node, cannot be null

        if (parent.isTwoNode()) {
            return parent.rightChild == child && parent.leftChild.isTwoNode();
        } else if (parent.isThreeNode()) {
            if (parent.centerChild == child) {
                return parent.leftChild.isTwoNode();
            } else if (parent.rightChild == child) {
                return parent.centerChild.isTwoNode();
            }
        } else if (parent.isFourNode()) {
            if (parent.centerLeftChild == child) {
                return parent.leftChild.isTwoNode();
            } else if (parent.centerRightChild == child) {
                return parent.centerLeftChild.isTwoNode();
            } else if (parent.rightChild == child) {
                return parent.centerRightChild.isTwoNode();
            }
        }
        return false;
    }

    private TwoFourTreeItem mergeWithLeft(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//merges with left node if they are both 2 nodes, returns parent to ensure going down the correct path. cannot take in null pointer
        if(child==parent.leftChild) return child;
        
        //System.out.print("\nin mergeleft");
        
        if(child==parent.rightChild){
            if(parent.isThreeNode()){
                TwoFourTreeItem newnode= new TwoFourTreeItem(parent.centerChild.value1,parent.value2,child.value1);
                newnode.parent=parent;
                shiftdel(parent,parent.value2);
                if(!child.isLeaf){
                	newnode.isLeaf=false;
                    newnode.leftChild=parent.centerChild.leftChild;
                    newnode.leftChild.parent=newnode;
                    newnode.centerLeftChild = parent.centerChild.rightChild;
                    newnode.centerLeftChild.parent=newnode;
                    newnode.centerRightChild = child.leftChild;
                    newnode.centerRightChild.parent=newnode;
                    newnode.rightChild=child.rightChild;
                    newnode.rightChild.parent=newnode;
                }
                releasenode(child);
                parent.centerChild=releasenode(parent.centerChild);
                parent.rightChild=newnode;
                return newnode;
            }
            else if(parent.isFourNode()){
                TwoFourTreeItem newnode= new TwoFourTreeItem(parent.centerRightChild.value1,parent.value3,child.value1);
                newnode.parent=parent;
                shiftdel(parent,parent.value3);
                if(!child.isLeaf){
                	newnode.isLeaf=false;
                    newnode.leftChild=parent.centerRightChild.leftChild;
                    newnode.leftChild.parent=newnode;
                    newnode.centerLeftChild=parent.centerRightChild.rightChild;
                    newnode.centerLeftChild.parent=newnode;
                    newnode.centerRightChild=child.leftChild;
                    newnode.centerRightChild.parent=newnode;
                    newnode.rightChild=child.rightChild;
                    newnode.rightChild.parent=newnode;
                }
                releasenode(child);
                parent.centerRightChild=releasenode(parent.centerRightChild);
                parent.centerChild=parent.centerLeftChild;
                parent.centerLeftChild=null;
                parent.rightChild=newnode;
                return newnode;
              
            }
        }
        else if(child==parent.centerLeftChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(parent.leftChild.value1,parent.value1,child.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value1);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=parent.leftChild.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=parent.leftChild.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=child.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=child.rightChild;
                newnode.rightChild.parent=newnode;
            }
            parent.centerLeftChild=releasenode(child);
            parent.centerChild=parent.centerRightChild;
            parent.centerRightChild=null;
            releasenode(parent.leftChild);
            parent.leftChild=newnode;
            return newnode;
            
        }
        else if(child==parent.centerChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(parent.leftChild.value1,parent.value1,child.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value1);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=parent.leftChild.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=parent.leftChild.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=child.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=child.rightChild;
                newnode.rightChild.parent=newnode;
            }
            parent.centerChild=releasenode(child);
            releasenode(parent.leftChild);
            parent.leftChild=newnode;
            return newnode;
            
        }
        else if(child==parent.centerRightChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(parent.centerLeftChild.value1,parent.value2,child.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value3);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=parent.centerLeftChild.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=parent.centerLeftChild.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=child.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=child.rightChild;
                newnode.rightChild.parent=newnode;
            }
            releasenode(child);
            releasenode(parent.rightChild);
            parent.centerChild=parent.centerLeftChild;
            parent.centerLeftChild=null;
            parent.rightChild=newnode;
            return newnode;
            
        }
        
        return child;
    }
    
    private TwoFourTreeItem mergeWithRight(TwoFourTreeItem child, TwoFourTreeItem parent) {
    	//merges with right node if they are both 2 nodes, returns parent to ensure going down the correct path. cannot take in null pointer

        if(child==parent.rightChild) return child;
        
        //System.out.print("\nin mergeright");
        
        if(child==parent.leftChild){
            if(parent.isThreeNode()){
                TwoFourTreeItem newnode= new TwoFourTreeItem(child.value1,parent.value1,parent.centerChild.value1);
                newnode.parent=parent;
                shiftdel(parent,parent.value1);
                if(!child.isLeaf){
                	newnode.isLeaf=false;
                    newnode.leftChild=child.leftChild;
                    newnode.leftChild.parent=newnode;
                    newnode.centerLeftChild=child.rightChild;
                    newnode.centerLeftChild.parent=newnode;
                    newnode.centerRightChild=parent.centerChild.leftChild;
                    newnode.centerRightChild.parent=newnode;
                    newnode.rightChild=parent.centerChild.rightChild;
                    newnode.rightChild.parent=newnode;
                }
                releasenode(child);
                parent.centerChild=releasenode(parent.centerChild);
                parent.leftChild=newnode;
                return newnode;
            }
            else if(parent.isFourNode()){
                TwoFourTreeItem newnode= new TwoFourTreeItem(child.value1,parent.value1,parent.centerLeftChild.value1);
                newnode.parent=parent;
                shiftdel(parent,parent.value1);
                if(!child.isLeaf){
                	newnode.isLeaf=false;
                    newnode.leftChild=child.leftChild;
                    newnode.leftChild.parent=newnode;
                    newnode.centerLeftChild=child.rightChild;
                    newnode.centerLeftChild.parent=newnode;
                    newnode.centerRightChild=parent.centerLeftChild.leftChild;
                    newnode.centerRightChild.parent=newnode;
                    newnode.rightChild=parent.centerLeftChild.rightChild;
                    newnode.rightChild.parent=newnode;
                }
                releasenode(child);
                parent.centerLeftChild=releasenode(parent.centerLeftChild);
                parent.centerChild=parent.centerRightChild;
                parent.centerRightChild=null;
                parent.leftChild=newnode;
                return newnode;
              
            }
        }
        else if(child==parent.centerLeftChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(child.value1,parent.value2,parent.centerRightChild.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value2);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=child.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=child.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=parent.centerRightChild.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=parent.centerRightChild.rightChild;
                newnode.rightChild.parent=newnode;
            }
            parent.centerLeftChild=releasenode(child);
            parent.centerRightChild=releasenode(parent.centerRightChild);
            parent.centerChild=newnode;
            return newnode;
            
        }
        else if(child==parent.centerChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(child.value1,parent.value2,parent.rightChild.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value2);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=child.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=child.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=parent.rightChild.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=parent.rightChild.rightChild;
                newnode.rightChild.parent=newnode;
            }
            parent.centerChild=releasenode(child);
            releasenode(parent.rightChild);
            parent.rightChild=newnode;
            return newnode;
            
        }
        else if(child==parent.centerRightChild){
            
            TwoFourTreeItem newnode= new TwoFourTreeItem(parent.centerLeftChild.value1,parent.value2,child.value1);
            newnode.parent=parent;
            shiftdel(parent,parent.value2);
            if(!child.isLeaf){
            	newnode.isLeaf=false;
                newnode.leftChild=parent.centerLeftChild.leftChild;
                newnode.leftChild.parent=newnode;
                newnode.centerLeftChild=parent.centerLeftChild.rightChild;
                newnode.centerLeftChild.parent=newnode;
                newnode.centerRightChild=child.leftChild;
                newnode.centerRightChild.parent=newnode;
                newnode.rightChild=child.rightChild;
                newnode.rightChild.parent=newnode;
            }
            parent.centerRightChild=releasenode(child);
            parent.centerLeftChild=releasenode(parent.centerLeftChild);
            parent.centerChild=newnode;
            return newnode;
            
        }
        
        return child;
    }
    
    private void shiftadd(TwoFourTreeItem node, int val) {
    	//shifts values to add value in correct spot. cannot be null
        if(node.isFourNode()) return;
        
        //System.out.print("\nin shiftadd");
        
        if(node.values==0) {
        	node.value1=val;
        }
        
        else if(node.isTwoNode()) {
        	if(val< node.value1){
        		node.value2= node.value1;
        		node.value1=val;
        		}
        	else {
        		node.value2=val;
        	}	    			
        }
        else if (node.isThreeNode()) {
        	if(val<node.value1) {
        		node.value3=node.value2;
        		node.value2=node.value1;
        		node.value1=val;
        		}
        	else if(val<node.value2){
        		node.value3=node.value2;
        		node.value2=val;
        		}
        	else {
        		node.value3=val;
        	}
        }
        
        //increment num of values
        node.values++;
        
    }
    
    private void shiftdel(TwoFourTreeItem node, int val) {
    	//shift deletes to ensure correct order , does not touch pointers, only values
    	
    	//System.out.print("\nin shiftdel");
        if(node.isTwoNode()) {
        	node.value1=0;
        }
        
        else if (node.isThreeNode()) {
			if (val==node.value1) {
				node.value1=node.value2;
				node.value2=0;
			}
			else {
				node.value2=0;
			}
		}
		else if(node.isFourNode()) {
			if (val==node.value1) {
				node.value1=node.value2;
				node.value2=node.value3;
				node.value3=0;
			}
			else if(val==node.value2) {
				node.value2=node.value3;
				node.value3=0;
			}
			else if(val==node.value3) {
				node.value3=0;
			}
		}
		
		node.values--;
        
    }

    private TwoFourTreeItem mergeintofournode(TwoFourTreeItem walk){
    	//merges 3 2nodes into 1 node, cannot take in null and must have children
    	
    	//System.out.print("\nin 4nmerge");
       //add values to root
		walk.value3 = walk.rightChild.value1;
        walk.value2 = walk.value1;
        walk.value1 = walk.leftChild.value1;
        walk.values = 3;
        
        //fix root pointers    		
        walk.centerLeftChild = walk.leftChild.rightChild;
        walk.centerRightChild = walk.rightChild.leftChild;
        walk.leftChild = walk.leftChild.leftChild;
        walk.rightChild = walk.rightChild.rightChild;
        
        //fix parent pointers
        if (walk.leftChild != null) walk.leftChild.parent = walk;
        if (walk.centerLeftChild != null) walk.centerLeftChild.parent = walk;
        if (walk.centerRightChild != null) walk.centerRightChild.parent = walk;
        if (walk.rightChild != null) walk.rightChild.parent = walk;
        
        return walk;			
	
   }

	public boolean deleteValue(int value) {
		//deletes a value!! yayyy! takes in a value
		//returns true if success and false if fail
		
        //empty tree
        if(root==null) {
        return false;
        }
        
        //does value exist
        if(hasValue(value)) {
        
            TwoFourTreeItem walk = root;
            boolean found = false;
            
            //root is 2 node and childrem are two nodes, merges root into 4 node
            if(walk.rightChild!=null && walk.leftChild!= null && walk.isTwoNode() && walk.leftChild.isTwoNode() && walk.rightChild.isTwoNode()) {
            	root=mergeintofournode(walk);
            }
            //is root a 2 node, if yes checks if it needs a shift or a merge. 
            //it is outside the while loop because all helper functions deal with siblings and root is the only node with none
            if(root.isTwoNode()){
            	if(value==root.value1) {
            		TwoFourTreeItem find=root.rightChild;
            		            		
            		while(!find.isLeaf){
        		        find=find.leftChild;
        		    }
        		    walk.value1=find.value1;
        		    value=find.value1;
        		                		
            	}
            	else if(value<root.value1) {
            		
            		if(root.leftChild.isTwoNode()) {
	            		root.leftChild.centerChild=root.leftChild.rightChild;
	            		root.leftChild.rightChild=root.rightChild.leftChild;
	            		root.leftChild.rightChild.parent=root.leftChild;
	            		if(root.rightChild.isThreeNode()){
	                        root.rightChild.leftChild=root.rightChild.centerChild;
	                        root.rightChild.centerChild=null;
	                    }
	                    else{
	                        root.rightChild.leftChild=root.rightChild.centerLeftChild;
	                        root.rightChild.centerChild=root.rightChild.centerRightChild;
	                        root.rightChild.centerLeftChild=null;
	                        root.rightChild.centerRightChild=null;
	                    }
	            		shiftadd(root.leftChild,root.value1);
	                    shiftadd(root,root.rightChild.value1);
	                    shiftdel(root.rightChild,root.rightChild.value1);
	                    shiftdel(root,root.value1);
	                   
	                    walk=walk.leftChild;
            		}
            		else {
            			walk=walk.leftChild;
            		}
            	}
            	else if(value>root.value1) {
            		
            		if(root.rightChild.isTwoNode()) {
	            		root.rightChild.centerChild=root.rightChild.leftChild;
	            		root.rightChild.leftChild=root.leftChild.rightChild;
	            		root.rightChild.leftChild.parent=root.rightChild;
	            		if(root.leftChild.isThreeNode()){
	                        root.leftChild.rightChild=root.leftChild.centerChild;
	                        root.leftChild.centerChild=null;
	                        shiftadd(root,root.leftChild.value2);
	                        shiftdel(root.leftChild,root.leftChild.value2);
	                    }
	                    else{
	                        root.leftChild.rightChild=root.leftChild.centerRightChild;
	                        root.leftChild.centerChild=root.leftChild.centerLeftChild;
	                        root.leftChild.centerLeftChild=null;
	                        root.leftChild.centerRightChild=null;
	                        shiftadd(root,root.leftChild.value3);
	                        shiftdel(root.leftChild,root.leftChild.value3);
	                    }
	            		shiftadd(root.rightChild,root.value2);
	                    shiftdel(root,root.value2);
	                    
	                    walk=walk.rightChild;
            		}
            		else {
            			walk=walk.rightChild;
            		}
            		
            	}
            	
            }    
            
            	
            while(!found) {
            	//enters while loop until found is at a leaf node
            	
            	//System.out.print("\nin while del");
            	
                //checks if current is a 2node then checks if it can borrow, then prioritized right merge and then left merge as final resort
                if(walk.isTwoNode()){
            	    if(!canBorrowFromRight(walk,walk.parent).equals("F") || !canBorrowFromLeft(walk,walk.parent).equals("F")){
            	        walk=borrowFromsibling(walk,walk.parent);
            	    }
            	    else if(canMergeWithRight(walk,walk.parent)){
            	        walk=mergeWithRight(walk,walk.parent);
            	    }
            	    else if(canMergeWithLeft(walk,walk.parent)){
            	        walk=mergeWithLeft(walk,walk.parent);
            	    }
            	}
                
            	//found value in current node
            	if(value==walk.value1 || value==walk.value2 || value==walk.value3) {
            		
            		//System.out.print("in found");
            		
            		
            		//leaf node
            		if (walk.isLeaf) {
            			
            			//System.out.print("del leaf");
            		    
            			//tree has one node case
            			if(walk.isRoot()) {
            				if(walk.isTwoNode()) {
            				    releasenode(walk);
                			}
                			else{
                			    shiftdel(walk,value);
                			}
                			return true;	
            			}
            			
            			//leaf is a 2 node
            			if(walk.isTwoNode()) {
                    	    if(!canBorrowFromRight(walk,walk.parent).equals("F") ||!canBorrowFromLeft(walk,walk.parent).equals("F")){
                    	        walk=borrowFromsibling(walk,walk.parent);
                    	    }
                    	    else if(canMergeWithRight(walk,walk.parent)){
                    	        walk=mergeWithRight(walk,walk.parent);
                    	    }
                    	    else if(!canBorrowFromLeft(walk,walk.parent).equals("F")){
                    	        walk=mergeWithLeft(walk,walk.parent);
                    	    }
                    	}
            			
            			//leaf is 3 or 4 node
            			else {
            			    shiftdel(walk,value);
            			    return true;
            			}
            		
            		}
            		
            		//value is not in leaf
            		//replaces the value with successor and then changes value to successor, ensuring value is in leaf.
            		TwoFourTreeItem find=walk;
            		TwoFourTreeItem hold=walk;
            		
            		//System.out.print("findwalk");
            		
            		
            		if(value==walk.value1){
            		    
            		    if(walk.isThreeNode()){
            		        hold=walk.centerChild;
            		        find=walk.centerChild;
            		     }
            		    else if(walk.isFourNode()){
            		        hold=walk.centerLeftChild;
            		        find=walk.centerLeftChild;
            		    }
            		    while(!find.isLeaf){
            		        find=find.leftChild;
            		    }
            		    walk.value1=find.value1;
            		    value=find.value1;
            		    
            		    walk=hold;
            		}
            		else if(value==walk.value2){
            		    if(walk.isThreeNode()){
            		         hold=walk.rightChild;
            		        find=walk.rightChild;
            		    }
            		    else{
            		        hold=walk.centerRightChild;
            		        find=walk.centerRightChild;
            		        
            		    }
            		    while(!find.isLeaf){
            		        find=find.leftChild;
            		    }
            		    walk.value2=find.value1;
            		    value=find.value1;
            		    walk=hold;
            		}
            		else{
            		    hold=find.rightChild;
            		    find=walk.rightChild;
            		    
            		    while(!find.isLeaf){
            		        find=find.leftChild;
            		    }
            		    walk.value3=find.value1;
            		    value=find.value1;
            		    walk=hold;
            		} 
            	}
            		
            	
            	
            	
            	//searching for value
            	//value is less that val1
                else if(value<walk.value1){
                    walk=walk.leftChild;
                }
                
                //value is less than val2
                else if(value<walk.value2 && (walk.isThreeNode() || walk.isFourNode())){
                    if(walk.isThreeNode()){
                        walk=walk.centerChild;
                    }
                    
                    else{
                        walk=walk.centerLeftChild;
                    }
                }
                    
                //value is less than val3
                else if(value<walk.value3 && walk.isFourNode()){
                    walk=walk.centerRightChild;
                }
            
                //value is in right node
                else{
                    walk=walk.rightChild;
                    
                }	
            
            	//System.out.print("\nsearching");
            }
        }
        
        return false;  
		
	}
	    
    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
    	root=null;  	
    	
    }
}


