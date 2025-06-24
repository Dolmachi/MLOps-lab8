# MLOps Lab 8

## Main

### Executor
- spark.executor.instances                      1
- spark.executor.cores                          2
- spark.executor.memory                         5g
- spark.executor.memoryOverhead                 1g

- spark.kubernetes.executor.request.cores       2
- spark.kubernetes.executor.request.memory      6g
- spark.kubernetes.executor.limit.cores         2
- spark.kubernetes.executor.limit.memory        7g

### Driver
- spark.driver.cores                            2
- spark.driver.memory                           2g
- spark.driver.memoryOverhead                   1g

- spark.kubernetes.executor.request.cores       2
- spark.kubernetes.executor.request.memory      3g
- spark.kubernetes.executor.limit.cores         2
- spark.kubernetes.executor.limit.memory        4g


## Datamart

### Executor
- spark.executor.instances                      1
- spark.executor.cores                          2
- spark.executor.memory                         4g
- spark.executor.memoryOverhead                 1g

- spark.kubernetes.executor.request.cores       2
- spark.kubernetes.executor.request.memory      5g
- spark.kubernetes.executor.limit.cores         2
- spark.kubernetes.executor.limit.memory        6g

### Driver
- spark.driver.cores                            2
- spark.driver.memory                           3g
- spark.driver.memoryOverhead                   1g

- spark.kubernetes.executor.request.cores       2
- spark.kubernetes.executor.request.memory      4g
- spark.kubernetes.executor.limit.cores         2
- spark.kubernetes.executor.limit.memory        5g