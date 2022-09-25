This application detects Quasi-Inconsistencies[^1] in rule bases.

The location of the rule base in text format can be specified by running the program with parameter "-f filename".

The input file has to have the following format:<br>
// Optional Comments<br>
head1, body11 body12 body13 body14;<br>
head2, body21 body22 body23 body24;<br>

Rules are seperated by semicolons(;) and within the rules body and head are seperated by comma(,).

The application will transform the rule base to a SAT encoding that can be checked for Quasi-Inconsistency.<br>
By default it will call the SAT solvers Glucose and MiniSAT with their integration into LogicNG[^2] to print out an Issue if the rule base is Quasi-Inconsistent.


[^1]: https://www.mthimm.de/pub/2020/Corea_2020b.pdf
[^2]: https://logicng.org/
