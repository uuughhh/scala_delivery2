class Bank(val allowedAttempts: Integer = 3) {

  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()

  // TODO
  // project task 2
  // create a new transaction object and put it in the queue
  // spawn a thread that calls processTransactions
  def addTransactionToQueue(
      from: Account,
      to: Account,
      amount: Double
  ): Unit = {
    val transaction = new Transaction(
      transactionsQueue,
      processedTransactions,
      from,
      to,
      amount,
      allowedAttempts
    )
    transactionsQueue.push(transaction)
    new Thread(new Runnable {
      def run {
        processTransactions
      }
    }).start
  }

  // TODO
  // project task 2
  // Function that pops a transaction from the queue
  // and spawns a thread to execute the transaction.
  // Finally do the appropriate thing, depending on whether
  // the transaction succeeded or not
  private def processTransactions: Unit = {
    transactionsQueue.synchronized {
      val iterator = transactionsQueue.iterator
      while (iterator.hasNext) {
        val transaction = transactionsQueue.pop
        transaction.run()
        if (transaction.status == TransactionStatus.PENDING) {
          transactionsQueue.push(transaction)
          //processTransactions
          new Thread(new Runnable {
            def run {
            processTransactions
            }
          }).start
        } else {
          processedTransactions.push(transaction)
        }
      }
    }

  }

  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }

}
