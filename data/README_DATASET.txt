# Intelligent Job Portal Demo Dataset

This dataset is designed for the current Java implementation.

Important: JobCorpus.java loads every *.txt file from ../data and stores each file as a list of lines.
IntelligentJobSearch.java asks for one keyword and applies KMP string matching to every line in every corpus file.

Suggested demo searches:
- Java
- Hyderabad
- Python
- Data Structures
- SQL
- Git
- Developer
- Internship

Expected behavior:
1. Put these .txt files inside the repository's data/ folder.
2. Run IntelligentJobSearch.java from the src directory.
3. Enter a keyword.
4. Matching files, lines, and match positions are printed.
5. Results are saved to results/kmp_results.txt.

The records are synthetic academic-demo data, not live job listings.
