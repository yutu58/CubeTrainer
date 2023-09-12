package cubes.tx2.solvers;

import java.util.Stack;

public class Tx2Iterator {
    Stack<Integer> stack;
    int possibleMoves;

    public Tx2Iterator(){
        this.stack = new Stack<>();
        this.possibleMoves = 9;
    }

    public void next() {
        if (this.stack.empty()) {
            this.stack.push(0);
            return;
        }
        int last = this.stack.pop();
        if (last != (possibleMoves - 1)) {
            int nextInt = last + 1;
            if (this.stack.size() > 0) {
                int prev = this.stack.peek();
                while (prev / 3 == nextInt / 3) {
                    nextInt++;
                }

            }
            if (nextInt < possibleMoves) {
                this.stack.push(nextInt);
                return;
            }
        }
        this.next();

        int prev = this.stack.peek();
        int next = 0;

        while (next / 3 == prev / 3) {
            next++;
        }
        this.stack.push(next);
//        if (this.stack.peek() == 0 || this.stack.peek() == 1) {
//            this.stack.push(2);
//        }
//        else {
//            this.stack.push(0);
//        }
    }

    public int[] toArr() {
        int[] res = new int[this.stack.size()];
        for (int i = 0; i < this.stack.size(); i++) {
            res[i] = this.stack.get(i);
        }
        return res;
    }

    public int getSize() {
        return this.stack.size();
    }


}
