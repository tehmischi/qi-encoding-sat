This application detects Quasi-Inconsistencies[^1] in rule bases.

The location of the rule base in text format can be specified by running the program with parameter "-f filename".<br>
If you want to change the solver, this can be done by using -solver SolverName, with the options being MiniSat, Glucose or Minicard.<br>
Passing the parameter --debug will print out the whole assignment for all boolean variables, even if they are not relevant for the issue.

The input file has to have the following format:<br>
// Optional Comments<br>
head1, body11 body12 body13 body14;<br>
head2, body21 body22 body23 body24;<br>

Two examples for basic rule files are provided in the examples folder.

Rules are seperated by semicolons(;) and within the rules body and head are seperated by comma(,).
Negated variables are prefixed with a minus (-) sign (e.g. -a means NOT a).

The application will transform the rule base to a SAT encoding that can be checked for Quasi-Inconsistency.<br>
By default it will call the SAT solver Glucose with its integration into LogicNG[^2] to print out an issue if the rule base is Quasi-Inconsistent.


[^1]: https://www.mthimm.de/pub/2020/Corea_2020b.pdf
[^2]: https://logicng.org/
