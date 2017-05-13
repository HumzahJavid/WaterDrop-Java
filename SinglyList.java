package waterdrop;

/**
 *
 * @author humzah
 */
public class SinglyList {

    int length;
    Node head;
    Node goal;

    public class Node {

        Pipe data;
        Node next;

        public Node(Pipe data) {
            this.data = data;
            this.next = null;
        }
    } //keep node as internal constructor

    public SinglyList() {
        this.length = 0;
        this.head = null;
        this.goal = null;
    }

    public Node add(Pipe value) {
        Node node = new Node(value);
        Node currentNode = this.head;

        //if the list is empty
        if ((currentNode == null) || (this.length == 0)) {
            this.head = node;
            this.length += 1;
            //return node;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = node;//? should be currentNode = node (because currentNode is the null one)
            this.length += 1;
            //return node;
        }
        return node;
    }

    public boolean isInList(Pipe pipe) {
        if (this.getIndexOf(pipe) == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Node getNodeAt(int index) {
        Node currentNode = this.head;
        int count = 1;

        if ((this.length == 0) || (index < 1) || (index > this.length)) {
            System.out.println("Node not found in list, given invalid index");
        } else {
            while (count < index) {
                currentNode = currentNode.next;
                count += 1;
            }
        }

        return currentNode;
    }

    public int getIndexOf(Pipe value) {
        Node currentNode = this.head;
        int index = 1;

        while (index < this.length) {
            if (currentNode.data == value) {
                return index;
            }
            currentNode = currentNode.next;
            index += 1;
        }
        //return -1 if current node not found 
        System.out.println("Node not found in list, given invalid data");
        return -1;
    }

    public void remove(int index) {
        Node currentNode = this.head;
        int count = 1;
        Node beforeNodeToDelete = null;
        Node nodeToDelete = null;
        Node afterNodeToDelete = null;

        //invalid position
        if ((index < 0) || (index > this.length)) {
            System.out.println("Node not found in list, given invalid index");
        } //remove first node
        else if (index == 1) {
            nodeToDelete = currentNode;
            afterNodeToDelete = currentNode.next;
            this.head = afterNodeToDelete;
            nodeToDelete = null;
            this.length -= 1;
        }//remove any other node
        else {
            while (count < index) {
                beforeNodeToDelete = currentNode;
                nodeToDelete = currentNode.next;
                currentNode = currentNode.next;
                count++;
            }
            //link the node before the one deleted, to the one after (effectively removing the node to be deleted)
            beforeNodeToDelete.next = nodeToDelete.next;
            //nodeToDelete.data.connected = false;
            nodeToDelete = null;
            this.length -= 1;
        }
    }

    public void remove(Pipe pipe) {
        Node currentNode = this.head;
        int count = 1;
        Node beforeNodeToDelete = null;
        Node nodeToDelete = null;
        Node afterNodeToDelete = null;
        
        int index = this.getIndexOf(pipe);

        //invalid position
        if ((index < 0) || (index > this.length)) {
            System.out.println("Node not found in list, given invalid index");
        } //remove first node
        else if (index == 1) {
            nodeToDelete = currentNode;
            afterNodeToDelete = currentNode.next;
            this.head = afterNodeToDelete;
            nodeToDelete = null;
            this.length -= 1;
        }//remove any other node
        else {
            while (count < index) {
                beforeNodeToDelete = currentNode;
                nodeToDelete = currentNode.next;
                currentNode = currentNode.next;
                count++;
            }
            //link the node before the one deleted, to the one after (effectively removing the node to be deleted)
            beforeNodeToDelete.next = nodeToDelete.next;
            //nodeToDelete.data.connected = false;
            nodeToDelete = null;
            this.length -= 1;
        }
    }

    public void setGoalNode(Pipe goalPipe) {
        this.goal = new Node(goalPipe);
    }
}
