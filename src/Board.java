
public class Board {
	
	private char[][] board;
	private int size;
	
	public Board(){
		this(3);
	}
	
	public Board(int size){
		this.size = size;
		board = new char[size][size];
		for(int i=0;i<size;i++)
			for(int j=0;j<size;j++)
				board[i][j]='-';
	}
	
	public char getBoardValue(int row, int col){
		return board[row][col];
	}
	
	public char getBoardValue(int index){
		return board[index/size][index%size];
	}
	
	public void printBoard(){
		String result = "";
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				result = result + " " + board[i][j];
			}
			result = result + "\n";
		}
		System.out.println(result);
	}
	
	public void printBoard(char[][] A){
		String result = "";
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				result = result + " " + A[i][j];
			}
			result = result + "\n";
		}
		System.out.println(result);
	}
	
	public void mark(int row, int col, char playerId) {
		board[row][col]=playerId;
    }
	
	public boolean tie() {
		if(win('O')||win('X'))
			return false;
		boolean result = true;
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(board[i][j]!='X'&&board[i][j]!='O'){
					result=false;
					break;
				}				
			}
		}
		return result;
    }
	
	public boolean win(int row, int col) {
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(board[i][j] == board[row][col]){
					if(winByRow(i,j))
						return true;
					if(winByCol(i,j))
						return true;
				}
			}
		}
		return winByDiagonal(row,col);
    }

	public boolean win(char player) {
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(board[i][j] == player){
					if(winByRow(i,j))
						return true;
					if(winByCol(i,j))
						return true;
				}
			}
		}
		return winByDiagonal(player);
    }
	
	public boolean winByCol(int row, int col) {
		boolean result=true;
		for(int i=0;i<size;i++){
			if(board[i][col]!=board[row][col]){
				result = false;
				break;
			}
		}
		return result;
    }
	
	public boolean winByRow(int row, int col) {
		boolean result = true;
		for(int i=0;i<size;i++){
			if(board[row][i]!=board[row][col]){
				result = false;
				break;
			}
		}
		return result;
    }
	
	public boolean winByDiagonal(int row, int col) {
		boolean result = true;
		for(int i=0;i<size;i++){
			if(board[i][i]!=board[row][col]){
				result = false;
				break;
			}		
		}
		if(result){
			return result;
		}
		result =true;
		for(int i=0;i<size;i++){
			if(board[i][size-i-1]!=board[row][col]){				
				result = false;
				break;
			}		
		}
		return result;
    }

	public boolean winByDiagonal(char player) {
		boolean result = true;
		for(int i=0;i<size;i++){
			if(board[i][i]!=player){
				result = false;
				break;
			}		
		}
		if(result){
			return result;
		}
		result =true;
		for(int i=0;i<size;i++){
			if(board[i][size-i-1]!=player){				
				result = false;
				break;
			}		
		}
		return result;
    }
	
	public void setInitial(){
		//board[0][0]='X';
		//board[2][2]='O';
		//board[1][0]='X';
		//board[2][0]='O';
	}
	
	private char[][] copyarray(char[][] B){
		char[][] A = new char[size][size];
		for(int i=0;i<size;i++)
			for(int j=0;j<size;j++)
				A[i][j] = B[i][j];
		return A;
	}

	public int computersTurn(){
		char[][] boardcopy = copyarray(board);
		int index = -1;
		int maximum = -10;
		int bestlevel = -1;
		for(int i = 0; i<size*size;i++){
			if(boardcopy[i/size][i%size]=='-'){
				boardcopy[i/size][i%size]='O';
				int[] returned = minimize(copyarray(boardcopy),"-");
				int value = returned[0];
				if(value>maximum){
					maximum = value;
					index = i;
					bestlevel = returned[1];
				}else if(value == maximum && value == -1){
					if(returned[1]>bestlevel){
						index = i;
						bestlevel = returned[1];
					}
				}else if(value == maximum && value == 1){
					if(returned[1]<bestlevel){
						index = i;
						bestlevel = returned[1];
					}
				}
				//System.out.println("i = "+i+", Value = "+value+", Level : "+returned[1]);
				//System.out.println("Copy");
				//printBoard(boardcopy);
				//System.out.println("Original");
				//printBoard();
				boardcopy[i/size][i%size]='-';
			}
		}
		//System.out.println("Returned index : "+index);
		return index;
	}

	private int[] maximize(char[][] boardcopy,String s) {
		//System.out.println("Maximize");
		char[][] temp = copyarray(board);
		int maximum = -10;
		board = copyarray(boardcopy);
		if(win('O'))
			maximum = 1;
		else if(win('X'))
			maximum = -1;
		else if(tie())
			maximum = 0;
		board = copyarray(temp);
		
		if(maximum!=-10){
			int[] ans = {maximum,1};
			return ans;
		}
		int bestlevel = 0;
		for(int i = 0; i<size*size;i++){
			if(boardcopy[i/size][i%size]=='-'){
				boardcopy[i/size][i%size]='O';
				int[] returned = minimize(copyarray(boardcopy),s+"-");
				int value = returned[0];
				//System.out.println(s+"i = "+i+", Value = "+value+", Level : "+returned[1]);
				if(value>maximum){
					maximum = value;
					bestlevel = returned[1];
				}else if(value == maximum && value == -1){
					if(returned[1]>bestlevel){
						bestlevel = returned[1];
					}
				}else if(value == maximum && value == 1){
					if(returned[1]<bestlevel){
						bestlevel = returned[1];
					}
				}
				boardcopy[i/size][i%size]='-';
			}
		}
		int[] ans = {maximum,bestlevel+1};
		return ans;
	}

	private int[] minimize(char[][] boardcopy,String s) {
		//System.out.println("Minimize");
		char[][] temp = copyarray(board);
		int minimum = 10;
		board = copyarray(boardcopy);
		if(win('O'))
			minimum = 1;
		else if(win('X'))
			minimum = -1;
		else if(tie())
			minimum = 0;
		board = copyarray(temp);
		
		if(minimum!= 10){
			int[] ans = {minimum,1};
			return ans;
		}
		int bestlevel=0;
		for(int i = 0; i<size*size;i++){
			if(boardcopy[i/size][i%size]=='-'){
				boardcopy[i/size][i%size]='X';
				int[] returned = maximize(copyarray(boardcopy),s+"-");
				int value = returned[0];
				//System.out.println(s+"i = "+i+", Value = "+value+", Level : "+returned[1]);
				if(value<minimum){
					minimum = value;
					bestlevel = returned[1];
				}else if(value == minimum && value == -1){
					if(returned[1]<bestlevel){
						bestlevel = returned[1];
					}
				}else if(value == minimum && value == 1){
					if(returned[1]>bestlevel){
						bestlevel = returned[1];
					}
				}
				boardcopy[i/size][i%size]='-';
			}
		}
		int[] ans = {minimum,bestlevel+1};
		return ans;
	}
}
