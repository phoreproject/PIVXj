package org.phorej.core;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hash Engineering Solutions
 * Date: 5/3/14
 * To change this template use File | Settings | File Templates.
 */
public class CoinDefinition {

    public static final String coinName = "Phore";
    public static final String coinTicker = "PHR";
    public static final String coinURIScheme = "phore";
    public static final String cryptsyMarketId = "155";
    public static final String cryptsyMarketCurrency = "PHR";

    public enum CoinPrecision {
        Coins,
        Millicoins,
    }
    public static final CoinPrecision coinPrecision = CoinPrecision.Coins;

    public static final String UNSPENT_API_URL = "https://chainz.cryptoid.info/phr/api.dws?q=unspent";
    public enum UnspentAPIType {
        BitEasy,
        Blockr,
        Abe,
        Cryptoid,
    };
    public static final UnspentAPIType UnspentAPI = UnspentAPIType.Cryptoid;

    public static final String BLOCKEXPLORER_BASE_URL_PROD = "http://explorer.dash.org/";    //blockr.io
    public static final String BLOCKEXPLORER_ADDRESS_PATH = "address/";             //blockr.io path
    public static final String BLOCKEXPLORER_TRANSACTION_PATH = "tx/";              //blockr.io path
    public static final String BLOCKEXPLORER_BLOCK_PATH = "block/";                 //blockr.io path
    public static final String BLOCKEXPLORER_BASE_URL_TEST = "http://test.explorer.dash.org/";

    public static final String DONATION_ADDRESS = "Xdeh9YTLNtci5zSL4DDayRSVTLf299n9jv";  //Hash Engineering donation DASH address

    enum CoinHash {
        SHA256,
        scrypt,
        x11
    };
    public static final CoinHash coinPOWHash = CoinHash.x11;

    public static boolean checkpointFileSupport = true;

    public static final int TARGET_TIMESPAN = (int)(1 * 60);  // 60 second difficulty cycle, on average.
    public static final int TARGET_SPACING = (int)(1 * 60);  // 60 seconds per block.
    public static final int INTERVAL = TARGET_TIMESPAN / TARGET_SPACING;

    public static final int getInterval(int height, boolean testNet) {
            return INTERVAL;
    }
    public static final int getIntervalCheckpoints() {
            return INTERVAL;

    }
    public static final int getTargetTimespan(int height, boolean testNet) {
            return TARGET_TIMESPAN;    //72 min
    }

    public static int spendableCoinbaseDepth = 50; //main.h: static const int COINBASE_MATURITY
    public static final long MAX_COINS = 1000000000;                 //main.h:  MAX_MONEY

    public static final long DEFAULT_MIN_TX_FEE = 10000;   // MIN_TX_FEE
    public static final long DUST_LIMIT = 30000; //main.h CTransaction::GetMinFee        0.01 coins
    public static final long INSTANTX_FEE = 100000; //0.001 DASH (updated for 12.1)
    public static final boolean feeCanBeRaised = false;

    //
    // Phore 0.12
    //
    public static final int PROTOCOL_VERSION = 70002;          //version.h PROTOCOL_VERSION
    public static final int MIN_PROTOCOL_VERSION = 70002;        //version.h MIN_PROTO_VERSION

    public static final int BLOCK_CURRENTVERSION = 2;   //CBlock::CURRENT_VERSION
    public static final int MAX_BLOCK_SIZE = 1 * 1000 * 1000;


    public static final boolean supportsBloomFiltering = true; //Requires PROTOCOL_VERSION 70000 in the client

    public static final int Port    = 11771;       //protocol.h GetDefaultPort(testnet=false)
    public static final int TestPort = 11773;     //protocol.h GetDefaultPort(testnet=true)

    /** Zerocoin starting block height */
    public static final long TESTNET_ZEROCOIN_STARTING_BLOCK_HEIGHT = 201576;
    public static final long MAINNET_ZEROCOIN_STARTING_BLOCK_HEIGHT = 89994;

    //
    //  Production
    //
    public static final int AddressHeader = 55;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS
    public static final int p2shHeader = 13;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS
    public static final int dumpedPrivateKeyHeader = 128;   //common to all coins
    public static final long PacketMagic = 0x91c4fde9;  //0x91, 0xc4, 0xfd, 0xe9;
    public static final long TestnetPacketMagic = 0x477665ba; // 0x47, 0x76, 0x65, 0xba,

    //Genesis Block Information from main.cpp: LoadBlockIndex
    static public long genesisBlockDifficultyTarget = 0x207fffff;         //main.cpp: LoadBlockIndex
    static public long genesisBlockTime = 1505224800L;                       //main.cpp: LoadBlockIndex
    static public long genesisBlockNonce = 12345;                         //main.cpp: LoadBlockIndex
    static public String genesisHash = "2b1a0f66712aad59ad283662d5b919415a25921ce89511d73019107e380485bf";  //main.cpp: hashGenesisBlock
    static public String genesisMerkleRoot = "894177137a45952cfed89dd395e7fc85208a53548f34defc7c1a85cb0736b3a3";
    static public int genesisBlockValue = 0;                                                              //main.cpp: LoadBlockIndex

    //net.cpp strDNSSeed
    static public String[] dnsSeeds = new String[] {
        "dns0.phore.io",
        "phore.seed.rho.industries"
    };

    public static int minBroadcastConnections = 3;   //0 for default; we need more peers.

    //
    // TestNet - DASH
    //
    public static final boolean supportsTestNet = true;
    public static final int testnetAddressHeader = 139;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS_TEST
    public static final int testnetp2shHeader = 19;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS_TEST
    public static final long testnetPacketMagic = 0x457665ba;      //
    public static final String testnetGenesisHash =  "2b1a0f66712aad59ad283662d5b919415a25921ce89511d73019107e380485bf";
    static public long testnetGenesisBlockDifficultyTarget = (0x1e0ffff0);         //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockTime = 1454124731L;                       //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockNonce = (2402015L);                         //main.cpp: LoadBlockIndex





    //main.cpp GetBlockValue(height, fee)
    public static final Coin GetBlockReward(int nHeight)
    {
        Coin nSubsidy;
        if (nHeight == 0) {
            nSubsidy = Coin.valueOf(17500000, 0);
        } else if (nHeight > 0 && nHeight <= 200) {
            nSubsidy = Coin.valueOf(2500, 0);
        } else if (nHeight > 200 && nHeight <= 250000) {
            nSubsidy = Coin.valueOf(7, 0);
        } else if (nHeight > 250000 && nHeight <= 518399) {
            nSubsidy = Coin.valueOf(4, 5);
        } else if (nHeight > 518399 && nHeight <= 1036798) {
            nSubsidy = Coin.valueOf(3, 6);
        } else {
            nSubsidy = Coin.valueOf(2, 7);
        }
        return nSubsidy;
    }

    public static int subsidyDecreaseBlockCount = 4730400;     //main.cpp GetBlockValue(height, fee)

    public static BigInteger proofOfWorkLimit = Utils.decodeCompactBits(0x207fffffL);  //main.cpp bnProofOfWorkLimit (~uint256(0) >> 20); // digitalcoin: starting difficulty is 1 / 2^12

    static public String[] testnetDnsSeeds = new String[] {
            "pivx-testnet.seed.fuzzbawls.pw",
            "pivx-testnet.seed2.fuzzbawls.pw",
            "s3v3nh4cks.ddns.net"
    };
    //from main.h: CAlert::CheckSignature
    public static final String SATOSHI_KEY = "04659d53bd8f7ad9d34a17281febedac754e5a6eb136142d3a9c6c0ea21b6ed7498ceb3d872eed00ae755f7aeadaeb1d9ab5e1a8f1e7efcd0ddcb39d4623c12790";
    public static final String TESTNET_SATOSHI_KEY = "04659d53bd8f7ad9d34a17281febedac754e5a6eb136142d3a9c6c0ea21b6ed7498ceb3d872eed00ae755f7aeadaeb1d9ab5e1a8f1e7efcd0ddcb39d4623c12790";

    /** The string returned by getId() for the main, production network where people trade things. */
    public static final String ID_MAINNET = "mainnet";
    /** The string returned by getId() for the testnet. */
    public static final String ID_TESTNET = "testnet";
    /** Unit test network. */
    public static final String ID_UNITTESTNET = "unittest";

    //checkpoints.cpp Checkpoints::mapCheckpoints
    public static void initCheckpoints(Map<Integer, Sha256Hash> checkpoints)
    {
        checkpoints.put(    0, Sha256Hash.wrap("2b1a0f66712aad59ad283662d5b919415a25921ce89511d73019107e380485bf"));
        checkpoints.put( 1000, Sha256Hash.wrap("8defd49579d63545f9e8cdda31f8503e0513328ca3f7428f33a915258c764d15"));
        checkpoints.put(10000, Sha256Hash.wrap("6af2431daa7456e4620e9493091648eeaac8ddfd53d8cff8101c26806e301d9a"));
        checkpoints.put(90000, Sha256Hash.wrap("a883d86273f02cb19252a878d1e0bda1e5321140480b08e3df9544d7b3d1ce56"));;
    }

    //Unit Test Information
    public static final String UNITTEST_ADDRESS = "XgxQxd6B8iYgEEryemnJrpvoWZ3149MCkK";
    public static final String UNITTEST_ADDRESS_PRIVATE_KEY = "XDtvHyDHk4S3WJvwjxSANCpZiLLkKzoDnjrcRhca2iLQRtGEz1JZ";

}
