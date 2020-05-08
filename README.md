# MacMoney
The program is compiled using Maven

To run the program do as follows:
1) compile the jar using Maven
2) create a file that has a list of filepaths to every available copy of the BlockChain, seperated using new lines
3) from a folder where you want to store the local copy of the blockchain, run the following command:
  a) java -jar [location of the compiled jar with dependencies] [location of you file from (2)] [your mining ID]
4) the program then runs until 10 blocks are appended to the BlockChain.
