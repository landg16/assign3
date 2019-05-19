package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 0 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	private int[][] result;
	private long elapsedTime;
	private int solveNum;
	private String strRes;
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		result = ints;
		strRes = "";
		solveNum = 0;
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		solveRec(result);
		elapsedTime =  System.currentTimeMillis() - startTime;
		return solveNum;
	}

	private void solveRec(int[][] tempResult) {
		if(solveNum == MAX_SOLUTIONS) return;
		List<Spot> st = new ArrayList<Spot>();
		for(int i = 0; i<SIZE; i++){
			for(int j = 0; j<SIZE; j++){
				if(tempResult[i][j] == 0){
					Spot tmp = new Spot(i, j, tempResult);
					st.add(tmp);
				}
			}
		}
		//System.out.println(st.size());
		if(st.size() == 0) {
			solveNum++;
			if(solveNum == 1){
				for(int k = 0; k<SIZE; k++){
					for(int j = 0; j<SIZE; j++){
						strRes += tempResult[k][j] + " ";
					}
					strRes += "\n";
				}
			}
			
			return;
		}
		Collections.sort(st);
		Spot tmp = st.get(0);
		for(int i = 0; i<tmp.possibilities.size(); i++){
			tempResult[tmp.x][tmp.y] = tmp.possibilities.get(i);
			int[][] newResult = new int[SIZE][SIZE];
			for(int k = 0; k<SIZE; k++){
				for(int j = 0; j<SIZE; j++){
					newResult[k][j] = tempResult[k][j];
				}
			}
			solveRec(newResult);
		}
	}
	
	public String getSolutionText() {
		return strRes;
	}
	
	public long getElapsed() {
		return elapsedTime;
	}
	private class Spot implements Comparable{
		private ArrayList<Integer> possibilities;
		private int x;
		private int y;
		private int[][] tempResult;
		
		public Spot(int x, int y,int[][] tempResult) {
			this.x = x;
			this.y = y;
			this.tempResult = tempResult;
			possibilities = new ArrayList<Integer>();
			fillPossibilities();
		}
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		public ArrayList<Integer> possibleArr (){
			return possibilities;
		}
		
		private void fillPossibilities(){
			for(int num = 1; num <= SIZE; num++){
				boolean toInsert = true;
				for(int i = 0; i<SIZE; i++){
					if(tempResult[i][getY()]==num){
						toInsert = false;
						break;
					}
				}
				
				for(int i = 0; i<SIZE; i++){
					if(tempResult[getX()][i]==num){
						toInsert = false;
						break;
					}
				}
				int stX = getX()/3; stX *= 3;
				int stY = getY()/3; stY *= 3;
				for(int i = stX; i<stX+PART; i++){
					for(int j = stY; j<stY+PART; j++){
						if(tempResult[i][j] == num){
							toInsert = false;
							break;
						}
					}
				}
				Integer curr = new Integer(num);
				if(toInsert) possibilities.add(curr);
			}
		}
		
		@Override
		public int compareTo(Object arg0) {
			return possibilities.size()-((Spot)arg0).possibleArr().size();
		}
	}

	@Override
	public String toString(){
		String res = "";
		for(int i = 0; i<result.length; i++){
			for(int j = 0; j<result.length; j++){
				res += result[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}
}
