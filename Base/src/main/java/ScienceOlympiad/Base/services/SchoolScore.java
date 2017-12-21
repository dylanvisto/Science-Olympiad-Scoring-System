package ScienceOlympiad.Base.services;

public class SchoolScore {

	private String schoolName;
	public int score;

		public SchoolScore(String name, int schoolScore) 	
		{
			schoolName=name;
			this.score = schoolScore;
		}
		
		public SchoolScore(String name)
		{
			schoolName=name;
			score=0;
		}
		
		public String getSchoolName() {
			return schoolName;
		}
		
		public void setSchoolName(String name) {
			this.schoolName = name;
		}
		
		public void setSchoolScore(int score)
		{
			this.score = score;
		}
		
		public int getSchoolScore()
		{
			return score;
		}
}
