import hashlib
import json

class Block:
    def __init__(self, index, timestamp, data, previousHash):
        self.index = index
        self.timestamp = timestamp
        self.data = data
        self.previousHash = previousHash
        self.hash = self.calculateHash()

    def calculateHash(self):
        return hashlib.sha256(str(str(self.index) + self.previousHash + self.timestamp + json.dumps(self.data)).encode('utf-8')).hexdigest()

class BlockChain:
    def __init__(self):
        self.chain = [self.createGenesisBlock()]

    def createGenesisBlock(self):
        return Block(0, "01/01/2000", "Origin", "0")

    def getLatestBlock(self):
        return self.chain[-1]

    def addBlock(self, newBlock):
        newBlock.previousHash = self.getLatestBlock().hash
        newBlock.hash = newBlock.calculateHash()
        self.chain.append(newBlock)

    def isChainValid(self):
        for i in range(len(self.chain)):
            if self.chain[i].data == "Origin":
                continue
            currentBlock = self.chain[i]
            previousBlock = self.chain[i - 1]

            if currentBlock.hash != currentBlock.calculateHash():
                return False
            if currentBlock.previousHash != previousBlock.hash:
                return False
        return True


def listChain(ledger):
    for i in ledger.chain:
        print(str(i.index) + "\t" +  i.timestamp + "\t" + json.dumps(i.data) + "\t" + i.previousHash + "\t" + i.hash)

sample = BlockChain()
sample.addBlock(Block(1, "10/10/2010", {"ammount": 4}, ""))
listChain(sample)
print(sample.isChainValid())
