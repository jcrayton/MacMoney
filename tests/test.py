import hashlib
import json

class Block:
    def __init__(self, index, timestamp, data, previousHash):
        self.nonce = 0;
        self.index = index
        self.timestamp = timestamp
        self.data = data
        self.previousHash = previousHash
        self.hash = self.calculateHash()

    def calculateHash(self):
        return hashlib.sha256(str(str(self.index) + self.previousHash + self.timestamp + json.dumps(self.data) + str(self.nonce)).encode('utf-8')).hexdigest()

    def mineBlock(self, difficulty):
        while(self.hash[:difficulty] != ("0" * difficulty)):
            self.hash = self.calculateHash()
            self.nonce = self.nonce + 1;
        print("Block Mined" + self.hash)

class BlockChain:
    def __init__(self):
        self.chain = [self.createGenesisBlock()]
        self.difficulty = 1;

    def createGenesisBlock(self):
        return Block(0, "01/01/2000", "Origin", "0",)

    def getLatestBlock(self):
        return self.chain[-1]

    def addBlock(self, newBlock):
        newBlock.previousHash = self.getLatestBlock().hash
        newBlock.mineBlock(self.difficulty);
        self.chain.append(newBlock)

    def isChainValid(self):
        for i in range(len(self.chain)):
            if self.chain[i].data == "Origin":
                continue
            currentBlock = self.chain[i]
            previousBlock = self.chain[i - 1]

            if currentBlock.hash != currentBlock.calculateHash():
                print(currentBlock.hash)
                print(currentBlock.calculateHash())
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
