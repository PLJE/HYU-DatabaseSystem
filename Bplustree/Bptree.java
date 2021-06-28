
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Ascending implements Comparator<Integer> {
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }
}

public class Bptree {

    static int degree;
    static Node root = null;

    static class Node { //Node class
        static class key implements Comparable<key> {
            key(int key_, int value_, Node child_) {
                key = key_;
                value = value_;
                child = child_; //its left child
            }

            key(int key_, int value_) {
                key = key_;
                value = value_;
                child = null;
            }

            public int compareTo(key k) {
                if (this.key < k.key) {
                    return -1;
                } else if (this.key > k.key) {
                    return 1;
                }
                return 0;
            }

            int key;
            int value;
            Node child;
        }

        Node parent;
        Node right;//if current node is leaf node, it may point its right sibling
        int degree;
        List<key> l;

        Node() {
            parent = null;
            l = new ArrayList<>();
            right = null;
        }

        void sorting() { //keys must be sorted
            Collections.sort(this.l);
        }

        boolean is_leaf() { //if current node is leaf, it may return true
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).child != null) {
                    return false;
                }
            }
            return true;
        }

        void delete(int key) { //delete specific key
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).key == key) {
                    l.remove(i);
                }
            }
        }
    }

    public static void findroot() { //find root
        while (true) {
            if (root.parent == null)
                return;
            else
                root = root.parent;
        }
    }

    public static Node where_go(int key) {
        Node tmp = root;//return the node that contains key

        while (tmp.is_leaf() == false) {
            int size = tmp.l.size();
            if (tmp.l.get(0).key > key) {
                tmp = tmp.l.get(0).child;
                continue;
            } else if (tmp.l.get(size - 1).key < key) {
                tmp = tmp.right;
                continue;
            } else {
                for (int i = 0; i < size - 1; i++) {
                    if (tmp.l.get(i).key < key && key < tmp.l.get(i + 1).key) {
                        tmp = tmp.l.get(i + 1).child;
                        break;
                    }
                }
            }
        }
        return tmp;
    }

    public static void leaf_split(Node where, int key, int val) { //it is used when overflow happens at leaf node
        Node right = new Node();
        int left_num = degree / 2;
        where.l.add(new Node.key(key, val));
        where.sorting();
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = left_num; i < where.l.size(); i++) {
            int x = where.l.get(i).key;
            int xv = where.l.get(i).value;
            right.l.add(new Node.key(x, xv));
            a.add(x);
        }
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < where.l.size(); j++) {
                if (a.get(i) == where.l.get(j).key) {
                    where.l.remove(j);
                }
            }
        }
        int up_key = right.l.get(0).key;
        int up_value = right.l.get(0).value;
        //where.right = right;
        if (where.parent == null) {
            Node Nnew = new Node();
            Nnew.l.add(new Node.key(up_key, up_value));
            Nnew.l.get(0).child = where;
            Nnew.right = right;
            where.parent = Nnew;
            right.parent = Nnew;
            if(where.right != null){
                right.right= where.right;
                where.right=right;
            }else {
                where.right = right;
            }
        } else { //where.parent exists, compare the number and decide the location
            if (where.parent.l.get(where.parent.l.size() - 1).key < up_key) {
                where.parent.l.add(new Node.key(up_key, up_value));
                where.parent.sorting();
                where.parent.l.get(where.parent.l.size() - 1).child = where.parent.right;
                where.parent.right = right;
                right.parent = where.parent;
                right.right = where.right;
                where.right = right;
            } else if (where.parent.l.get(0).key > up_key) {
                where.parent.l.add(new Node.key(up_key, up_value));
                where.parent.sorting();
                where.parent.l.get(0).child = where;
                where.parent.l.get(1).child = right;
                right.parent = where.parent;
                right.right = where.right;
                where.right = right;
            } else {
                where.parent.l.add(new Node.key(up_key, up_value, where));
                where.parent.sorting();
                int idx = 0;
                for (int i = 0; i < where.parent.l.size(); i++) {
                    if (where.parent.l.get(i).key == up_key) {
                        idx = i + 1;
                    }
                }
                where.parent.l.get(idx).child = right;
                right.right = where.right;
                where.right = right;
                right.parent = where.parent;
                //right.right= where.right;
            }
        }
    }
    public static void non_leaf_split_delete(Node where){ //it's for non-leaf node split (delete process)
        int mid = where.l.get(where.l.size() / 2).key;
        int mid_value = where.l.get(where.l.size() / 2).value;

        Node right = new Node();
        for (int i = where.l.size() / 2 + 1; i < where.l.size(); i++) {
            int k = where.l.get(i).key;
            int v = where.l.get(i).value;
            right.l.add(new Node.key(k, v));
        }
        for (int i = 0; i < right.l.size(); i++) {
            right.l.get(i).child = where.l.get(i + where.l.size() / 2 + 1).child;
            where.l.get(i + where.l.size() / 2 + 1).child.parent = right;
        }
        where.right.parent = right;
        right.right = where.right;
        where.right = where.l.get(where.l.size() / 2).child;

        for (int i = 0; i < where.l.size(); i++) {
            if (where.l.get(i).key == mid)
                where.l.remove(i);
            for (int j = 0; j < right.l.size(); j++) {
                if (where.l.get(i).key == right.l.get(j).key)
                    where.l.remove(i);
            }
        }
        if (where.parent == null) {
            Node Nnew = new Node();
            Nnew.l.add(new Node.key(mid, mid_value));
            Nnew.l.get(0).child = where;
            Nnew.right = right;
            where.parent = Nnew;
            right.parent = Nnew;
            root=Nnew;
            return;
        }
        else {
            if(where.parent.l.size()==0){
                where.parent.l.add(new Node.key(mid,mid_value));
                where.parent.right = right;
                where.parent.l.get(0).child=where;
                right.parent=where.parent;
            }else {
                if (where.parent.l.get(0).key > mid) {
                    where.parent.l.add(new Node.key(mid, mid_value));
                    where.parent.sorting();
                    where.parent.l.get(0).child = where;
                    where.parent.l.get(1).child = right;
                } else if (where.parent.l.get(where.parent.l.size() - 1).key < mid) {
                    where.parent.l.add(new Node.key(mid, mid_value));
                    where.parent.right = right;
                    where.parent.l.get(where.parent.l.size() - 1).child = where;
                } else {
                    where.parent.l.add(new Node.key(mid, mid_value));
                    where.parent.sorting();
                    for (int i = 0; i < where.parent.l.size(); i++) {
                        if (where.parent.l.get(i).key == mid) {
                            where.parent.l.get(i).child = where;
                            where.parent.l.get(i + 1).child = right;
                            break;
                        }
                    }
                }
                right.parent = where.parent;
                if (where.parent.l.size() >= degree) { //recursion is needed..
                    non_leaf_split_delete(where.parent);
                } else {
                    return;
                }
            }
        }
    }
    public static void non_leaf_split(Node where) {//non-leaf node split for inserting process, it is little bit different from non-leaf split for delete.
        int mid = where.l.get(where.l.size() / 2).key;
        int mid_value = where.l.get(where.l.size() / 2).value;
        Node right = new Node();
        for (int i = where.l.size() / 2 + 1; i < where.l.size(); i++) {
            int k = where.l.get(i).key;
            int v = where.l.get(i).value;
            right.l.add(new Node.key(k, v));
        }
        for (int i = 0; i < right.l.size(); i++) {
            right.l.get(i).child = where.l.get(i + where.l.size() / 2 + 1).child;
            where.l.get(i + where.l.size() / 2 + 1).child.parent = right;
        }
        where.right.parent = right;
        right.right = where.right;
        where.right = where.l.get(where.l.size() / 2).child;
        for (int i = 0; i < where.l.size(); i++) {
            if (where.l.get(i).key == mid)
                where.l.remove(i);
            for (int j = 0; j < right.l.size(); j++) {
                if (where.l.get(i).key == right.l.get(j).key)
                    where.l.remove(i);
            }
        }
        if (where.parent == null) { //should create parent
            Node Nnew = new Node();
            Nnew.l.add(new Node.key(mid, mid_value));
            Nnew.l.get(0).child = where;
            Nnew.right = right;
            where.parent = Nnew;
            right.parent = Nnew;
            return;
        } else { //compare the number and decide the location
            if (where.parent.l.get(0).key > mid) {
                where.parent.l.add(new Node.key(mid, mid_value));
                where.parent.sorting();
                where.parent.l.get(0).child = where;
                where.parent.l.get(1).child = right;
            } else if (where.parent.l.get(where.parent.l.size() - 1).key < mid) {
                where.parent.l.add(new Node.key(mid, mid_value));
                where.parent.right = right;
                where.parent.l.get(where.parent.l.size() - 1).child = where;
            } else {
                where.parent.l.add(new Node.key(mid, mid_value));
                where.parent.sorting();
                for (int i = 0; i < where.parent.l.size(); i++) {
                    if (where.parent.l.get(i).key == mid) {
                        where.parent.l.get(i).child = where;
                        where.parent.l.get(i + 1).child = right;
                        break;
                    }
                }
            }
            right.parent = where.parent;
            if (where.parent.l.size() >= degree) {
                non_leaf_split(where.parent);
            } else {
                return;
            }
        }
    }

    public static void insert(int key, int val) { //insert func
        if (root == null) {
            root = new Node();
            root.l.add(new Node.key(key, val));
            return;
        }
        Node where = where_go(key);
        if (where.l.size() < degree - 1) { //just insert and sort it.
            where.l.add(new Node.key(key, val));
            where.sorting();
        } else { //overflow occurs..-> split process
            leaf_split(where, key, val);
            if (where.parent.l.size() >= degree) {
                non_leaf_split(where.parent);
            }
        }
        findroot(); //find root after insertion!
    }
    public static void range_search(int a, int b) { //range search func
        Node start = root;
        while (start.is_leaf() == false) { //start node
            if (a < start.l.get(0).key) {
                start = start.l.get(0).child;
            } else if (start.l.get(start.l.size() - 1).key <= a) {
                start = start.right;
            } else {
                for (int i = 0; i < start.l.size() - 1; i++) {
                    if (start.l.get(i).key <= a && a < start.l.get(i + 1).key) {
                        start = start.l.get(i + 1).child;
                        break;
                    }
                }
            }
        }
        Node end = root;
        while (end.is_leaf() == false) { //end node
            if (b < end.l.get(0).key) {
                end = end.l.get(0).child;
            } else if (end.l.get(end.l.size() - 1).key <= b) {
                end = end.right;
            } else {
                for (int i = 0; i < end.l.size() - 1; i++) {
                    if (end.l.get(i).key <= b && b < end.l.get(i + 1).key) {
                        end = end.l.get(i + 1).child;
                        break;
                    }
                }
            }
        }
        Node tmp = start;
        while (true) {
            for (int i = 0; i < tmp.l.size(); i++) {
                if (tmp.l.get(i).key >= a && tmp.l.get(i).key <= b) {
                    System.out.println(tmp.l.get(i).key + " , " + tmp.l.get(i).value);
                }
            }
            tmp = tmp.right;
            if (tmp == end.right)
                break;
        }
    }

    public static void single_search(int key) { //single key search
        Node tmp =root;
        List<Node> l = new ArrayList<>(); // it may contains the path Nodes. if key exists, it is used.
        while (tmp.is_leaf() == false) {
            l.add(tmp);
            if (key < tmp.l.get(0).key) {
                tmp = tmp.l.get(0).child;
            } else if (tmp.l.get(tmp.l.size() - 1).key <= key) {
                tmp = tmp.right;
            } else {
                for (int i = 0; i < tmp.l.size() - 1; i++) {
                    if (tmp.l.get(i).key <= key && key < tmp.l.get(i + 1).key) {
                        tmp = tmp.l.get(i + 1).child;
                        break;
                    }
                }
            }
        }
        for(int i=0;i<tmp.l.size();i++){
            if(tmp.l.get(i).key == key){
                for(int j=0;j<l.size();j++){
                    for(int k=0;k<l.get(j).l.size();k++){
                        System.out.print(l.get(j).l.get(k).key+" ");
                    }
                    System.out.println();
                }
                System.out.println(tmp.l.get(i).value);
                return;
            }
        }
        System.out.println("NOT FOUND");
    }

    public static void revise(int del, int rep) { //in tree, find del key , and revise it with rep.
        Node tmp = root;
        while (tmp.is_leaf() == false) {
            for (int i = 0; i < tmp.l.size(); i++) {
                if (tmp.l.get(i).key == del) { //if del is in non_leaf node, then replace with rep
                    tmp.l.get(i).key = rep;
                    return;
                }
            }
            if (tmp.l.get(0).key > del) {
                tmp = tmp.l.get(0).child;
            } else if (tmp.l.get(tmp.l.size() - 1).key <= del) {
                tmp = tmp.right;
            } else {
                for (int i = 0; i < tmp.l.size() - 1; i++) {
                    if (tmp.l.get(i).key <= del && del < tmp.l.get(i + 1).key) {
                        tmp = tmp.l.get(i + 1).child;
                    }
                }
            }
        }
    }

    public static void non_leaf_merge(Node where) { //when underflow occurs at non-leaf node
        //first, it must find sibling for merging
        Node left = null;
        Node right = null;
        int least = (degree - 1) / 2;

        int idx = -1;
        for (int i = 0; i < where.parent.l.size(); i++) {
            if (where.parent.l.get(i).child == where)
                idx = i;
        }

        if (where.parent.l.size() == 1) {
            if (idx == 0) {
                left = null;
                right = where.parent.right;
            } else { //idx == -1
                left = where.parent.l.get(0).child;
                right = null;
                idx =where.parent.l.size();
            }
        }
        else {
            if (idx == 0) {
                left = null;
                right = where.parent.l.get(1).child;
            } else if (idx == where.parent.l.size() - 1) {
                left = where.parent.l.get(idx - 1).child;
                right = where.parent.right;
            } else if (idx == -1) {
                left = where.parent.l.get(where.parent.l.size() - 1).child;
                right = null;
                idx = where.parent.l.size();
            } else {
                left = where.parent.l.get(idx - 1).child;
                right = where.parent.l.get(idx + 1).child;
            }
        }//find left and right sibling

        if (left != null && where.parent != null ) {  //merge with left
            int k = left.parent.l.get(idx - 1).key;
            int v = left.parent.l.get(idx - 1).value;
            left.l.add(new Node.key(k, v, left.right));

            where.parent.delete(k);


            for (int i = 0; i < where.l.size(); i++) {
                k = where.l.get(i).key;
                v = where.l.get(i).value;
                Node tmp = where.l.get(i).child;
                left.l.add(new Node.key(k, v, tmp));
                where.l.get(i).child.parent = left;
            }
            where.right.parent = left;
            left.right = where.right;

            if (where.parent.l.size() == 0)
                where.parent.right = left;
            else if (idx - 1 == where.parent.l.size())//rightmost
                where.parent.right = left;
            else
                where.parent.l.get(idx - 1).child = left;

            if (left.l.size() >= degree) //overflow occurs
                non_leaf_split_delete(left); // should split this node
            if (where.parent == root && where.parent.l.size() == 0 ) {
                root = left; //left degree is okay, and root left should be root
                root.parent = null;
            } else if (where.parent.l.size() < least && where.parent != root) {
                non_leaf_merge(where.parent);
            }

        } else if (right != null &&where.parent!=null ) { //merge with right
            int k = where.parent.l.get(idx).key;
            int v = where.parent.l.get(idx).value;

            where.l.add(new Node.key(k, v, where.right));
            where.parent.delete(k);
            where.right = right.right;

            for (int i = 0; i < right.l.size(); i++) {
                k = right.l.get(i).key;
                v = right.l.get(i).value;
                Node tmp = right.l.get(i).child;
                where.l.add(new Node.key(k, v, tmp));
                right.l.get(i).child.parent = where;
            }
            right.right.parent = where;

            if (where.parent.l.size() == 0)
                where.parent.right = where;
            else if (idx == where.parent.l.size())//
                where.parent.right = where;
            else
                where.parent.l.get(idx).child = where;

            if (where.l.size() >= degree)
                non_leaf_split_delete(where);
            if (where.parent != root && where.parent.l.size() < least)
                non_leaf_merge(where.parent);
            else if (where.parent == root && where.parent.l.size() < 1) {
                root = where;
                root.parent = null;
            }
        }
    }

    public static void delete(int key) {
        Node ltmp = root; //leaf node that contains deletekey
        int least = (degree - 1) / 2; // nodes must have at least (degree-1)/2 keys

        while (ltmp.is_leaf() == false) {
            if (key < ltmp.l.get(0).key) {
                ltmp = ltmp.l.get(0).child;
            } else if (ltmp.l.get(ltmp.l.size() - 1).key <= key) {
                ltmp = ltmp.right;
            } else {
                for (int i = 0; i < ltmp.l.size() - 1; i++) {
                    if (ltmp.l.get(i).key <= key && key < ltmp.l.get(i + 1).key) {
                        ltmp = ltmp.l.get(i + 1).child;
                        break;
                    }
                }
            }
        }
        if(ltmp == root){
            ltmp.delete(key);
            return;
        }
        int fkey = ltmp.l.get(0).key; //before deleting, save the first key of ltmp. it will be used .
        Node left = findleft(ltmp); //find left node of ltmp

        if (ltmp.l.size() - 1 >= least) { //just delete
            boolean chk= false;
            if(ltmp.l.get(0).key == key)
                chk=true;
            ltmp.delete(key);
            if(chk==true)
                revise(fkey, ltmp.l.get(0).key);
            return; //delete is finished
        }
        //underflow occurs
        if (left != null && left.l.size() - 1 >= least && left.parent == ltmp.parent) {//borrow from left
            ltmp.delete(key);
            int k = left.l.get(left.l.size() - 1).key;
            int v = left.l.get(left.l.size() - 1).value;
            ltmp.l.add(new Node.key(k, v));
            ltmp.sorting();
            left.delete(k);
            revise(fkey, ltmp.l.get(0).key);
        }
        else if (ltmp.right != null && ltmp.right.l.size() - 1 >= least && ltmp.parent == ltmp.right.parent) {
            boolean chk = true; //borrow from right
            if (ltmp.l.get(0).key == key) { //if key is located at the first of node
                chk = false;
            }
            ltmp.delete(key);
            int k = ltmp.right.l.get(0).key;
            int v = ltmp.right.l.get(0).value;
            ltmp.l.add(new Node.key(k, v));
            ltmp.sorting();
            ltmp.right.delete(k);

            revise(ltmp.l.get(ltmp.l.size() - 1).key, ltmp.right.l.get(0).key);
            if (chk == false)
                revise(fkey, ltmp.l.get(0).key); //key is located at first, so revision is needed
        }
        else {//ltmp can't borrow from left nor right -> merging

            if (left != null && left.parent == ltmp.parent ) { //merge with left node
                ltmp.delete(key);
                int idx = -1;
                for (int i = 0; i < ltmp.parent.l.size(); i++) {
                    if (ltmp.parent.l.get(i).child == ltmp)
                        idx = i;
                }
                if (idx == -1)
                    idx = ltmp.parent.l.size();

                for (int i = 0; i < ltmp.l.size(); i++) {
                    int k = ltmp.l.get(i).key;
                    int v = ltmp.l.get(i).value;
                    left.l.add(new Node.key(k, v));
                }
                left.sorting();

                if (ltmp.right == null)
                    left.right = null;
                else
                    left.right = ltmp.right;

                if (idx == ltmp.parent.l.size())
                    ltmp.parent.right = left;
                else
                    ltmp.parent.l.get(idx).child = left;

                ltmp.parent.l.remove(idx - 1);

                if (ltmp.parent == root) {
                    if (ltmp.parent.l.size() >= 1)//root must have at least 1 key.
                        return;
                    else { //root has no key
                        root = left;
                        root.parent = null;
                    }
                } else {
                    if (ltmp.parent.l.size() >= least)
                        return;
                    else
                    {
                        non_leaf_merge(ltmp.parent); //underflow occurs
                    }
                }
            }

            else if (ltmp.right != null && ltmp.right.parent == ltmp.parent  ) { //merge with right node
                boolean chk = false;
                if(ltmp.l.get(0).key == key)
                    chk=true;

                ltmp.delete(key);

                for (int i = 0; i < ltmp.l.size(); i++) {
                    int k = ltmp.l.get(i).key;
                    int v = ltmp.l.get(i).value;
                    ltmp.right.l.add(new Node.key(k, v));
                }
                ltmp.right.sorting();

                int idx = -1;
                for (int i = 0; i < ltmp.parent.l.size(); i++) {
                    if (ltmp.parent.l.get(i).child == ltmp)
                        idx = i;
                }
                if (idx == -1)
                    idx = ltmp.parent.l.size();

                if (chk==true)
                    revise(key, ltmp.right.l.get(0).key);

                ltmp.parent.l.remove(idx);

                if (left != null)
                    left.right = ltmp.right;

                if (ltmp.parent == root) {
                    if (ltmp.parent.l.size() >= 1)
                        return;
                    else {
                        root = ltmp.right;
                        root.parent = null;
                    }
                } else {
                    if (ltmp.parent.l.size() < least) //underflow
                        non_leaf_merge(ltmp.parent);
                    else { //ltmp.parent must be mergerd
                        return;
                    }
                }
            }
        }
        findroot();
    }

    public static Node findleft(Node n) { //find left node of leaf node
        Node tmp = root;
        while (tmp.is_leaf() == false) {
            tmp = tmp.l.get(0).child;
        }
        if (tmp == n) //this leaf node has no left sibling. it is leftmost node
            return null;
        while (tmp != null) {
            if (tmp.right == n) {
                break;
            }
            tmp = tmp.right;
        }
        return tmp;
    }
    public static void main(String[] args) {
        String indexdat = args[1]; //it's index.dat file, args[2] is a number of input/delete.csv file
        switch (args[0]) {
            case "-c": //create index.dat and write the degree
                degree = Integer.parseInt(args[2]);
                try {
                    FileWriter w = new FileWriter(args[1], false);
                    w.write(args[2] + "\n");
                    w.flush();
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "-i": //insert
                String insertfile = args[2];
                try{
                    BufferedReader bfr =new BufferedReader(new FileReader(insertfile));
                    BufferedWriter bfw = new BufferedWriter(new FileWriter(indexdat,true));
                    String line="";
                    while((line=bfr.readLine())!=null){ //read input.csv
                        String [] num = line.split(",");
                        Integer key = Integer.parseInt(num[0]);
                        Integer value= Integer.parseInt(num[1]);

                        bfw.write(key.toString()); //record it in index.dat
                        bfw.write(",");
                        bfw.write(value.toString());
                        bfw.write("\n");
                    }
                    bfw.close();
                    bfr = new BufferedReader(new FileReader(indexdat));
                    String tmp="";
                    tmp =bfr.readLine();
                    degree = Integer.parseInt(tmp.toString());
                    while((tmp=bfr.readLine())!=null){ //read index.dat
                        if(tmp.contains(",")){ //insert
                            String[] num =tmp.split(",");
                            Integer key =Integer.parseInt(num[0]);
                            Integer value = Integer.parseInt(num[1]);
                            insert(key.intValue(),value.intValue());
                        }else{//delete
                            String dkey = tmp;
                            Integer key = Integer.parseInt(dkey);
                            delete(key.intValue());
                        }
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                break;
            case "-d": //delete
                String deletefile = args[2];
                try{
                    BufferedReader bfr = new BufferedReader(new FileReader(deletefile));
                    BufferedWriter bfw = new BufferedWriter(new FileWriter(indexdat,true));
                    String line="";
                    while((line=bfr.readLine())!=null){   //read delete.csv
                        String key = line;
                        Integer dkey = Integer.parseInt(key);
                        bfw.write(dkey.toString()); //record the keys
                        bfw.write("\n");
                    }bfw.close();

                    String tmp="";
                    bfr = new BufferedReader(new FileReader(indexdat)); //read index.dat
                    tmp = bfr.readLine();
                    degree=Integer.parseInt(tmp.toString());
                    while((tmp = bfr.readLine())!=null){
                        if(tmp.contains(",")){ //insert
                            String[] num =tmp.split(",");
                            Integer key =Integer.parseInt(num[0]);
                            Integer value = Integer.parseInt(num[1]);
                            insert(key.intValue(),value.intValue());
                        }else{//delete
                            String dkey = tmp;
                            Integer key = Integer.parseInt(dkey);
                            delete(key.intValue());
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                break;
            case "-r": //range search
                try{
                    BufferedReader bfr= new BufferedReader(new FileReader(indexdat));
                    String line="";
                    line = bfr.readLine();
                    degree = Integer.parseInt(line.toString());
                    while((line=bfr.readLine())!=null){ //read index.dat
                        if(line.contains(",")){
                            String[] num =line.split(",");
                            Integer key =Integer.parseInt(num[0]);
                            Integer value = Integer.parseInt(num[1]);
                            insert(key.intValue(),value.intValue());
                        }else{
                            String dkey = line;
                            Integer key = Integer.parseInt(dkey);
                            delete(key.intValue());
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                Integer start = Integer.parseInt(args[2]);
                Integer end = Integer.parseInt(args[3]);
                range_search(start.intValue(),end.intValue()); //call func
                break;
            case "-s": //single search
                try{
                    BufferedReader bfr= new BufferedReader(new FileReader(indexdat));
                    String line="";
                    line = bfr.readLine();
                    degree = Integer.parseInt(line.toString());
                    while((line=bfr.readLine())!=null){ //read index.dat
                        if(line.contains(",")){
                            String[] num =line.split(",");
                            Integer key =Integer.parseInt(num[0]);
                            Integer value = Integer.parseInt(num[1]);
                            insert(key.intValue(),value.intValue());
                        }else{
                            String dkey = line;
                            Integer key = Integer.parseInt(dkey);
                            delete(key.intValue());
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                Integer singlekey = Integer.parseInt(args[2]);
                single_search(singlekey.intValue());
                break;
        }
    }
}


