Design of benchmark client:

The benchmark client is designed with two loops that iterate for a fixed amount of time specified by the constant "DURATION". Each loop creates client connections to each of the respective servers. A counter is updated each time the loop iterates.

Outputs of the benchmark client:

With a duration of 30 ms, and each client sending 5,000 messages, the results are as follows:

Simple Server connections: 30.0
Threaded Server connections: 52.0

Simple Server rate: 1.0 connections/ms
Threaded Server rate: 1.7333333333333334 connections/ms

With a duration of 30 ms, and each client sending 50,000 messages, the results are as follows:

Simple Server connections: 9.0
Threaded Server connections: 30.0

Simple Server rate: 0.3 connections/ms
Threaded Server rate: 1.0 connections/ms


Reasoning:

The multithreaded server performs better than the single threaded server because it does not have to wait for the previous client to finish before it can accept another connection--in other words, the multithreaded server does not block the accept action. So the longer the process in the client, the better the multithreaded server will perform in comparison to the single threaded server as is shown in the output above.