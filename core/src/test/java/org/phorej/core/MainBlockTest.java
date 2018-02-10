package org.phorej.core;

import com.google.common.util.concurrent.ListenableFuture;
import org.phorej.params.MainNetParams;
import org.phorej.params.TestNet2Params;
import org.phorej.store.BlockStore;
import org.phorej.store.MemoryBlockStore;
import org.phorej.utils.BriefLogFormatter;
import org.phorej.wallet.Wallet;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by furszy on 6/25/17.
 */
public class MainBlockTest {

    @org.junit.Rule
    public ExpectedException thrown = ExpectedException.none();

    private BlockChain testNetChain;

    private Wallet wallet;
    private BlockChain chain;
    private BlockStore blockStore;
    private Address coinbaseTo;
    private static NetworkParameters PARAMS;
    private final StoredBlock[] block = new StoredBlock[1];
    private Transaction coinbaseTransaction;

    private static class TweakableTestNet2Params extends TestNet2Params {
        public void setMaxTarget(BigInteger limit) {
            maxTarget = limit;
        }
    }
    private static MainBlockTest.TweakableTestNet2Params testNet;

    private void resetBlockStore() {
        blockStore = new MemoryBlockStore(PARAMS);
    }

    @Before
    public void setUp() throws Exception {
        PARAMS = MainNetParams.get();
        testNet = new MainBlockTest.TweakableTestNet2Params();
        BriefLogFormatter.initVerbose();
        Context.propagate(new Context(testNet, 100, Coin.ZERO, false));
        testNetChain = new BlockChain(testNet, new Wallet(testNet), new MemoryBlockStore(testNet));
        Context.propagate(new Context(PARAMS, 100, Coin.ZERO, false));
        wallet = new Wallet(PARAMS) {
            @Override
            public void receiveFromBlock(Transaction tx, StoredBlock block, BlockChain.NewBlockType blockType,
                                         int relativityOffset) throws VerificationException {
                super.receiveFromBlock(tx, block, blockType, relativityOffset);
                MainBlockTest.this.block[0] = block;
                if (isTransactionRelevant(tx) && tx.isCoinBase()) {
                    MainBlockTest.this.coinbaseTransaction = tx;
                }
            }
        };
        wallet.freshReceiveKey();

        resetBlockStore();
        chain = new BlockChain(PARAMS, wallet, blockStore);

        coinbaseTo = wallet.currentReceiveKey().toAddress(PARAMS);
    }

    @Test
    public void testBasicChaining() throws Exception {
        // Check that we can plug a few blocks together and the futures work.
        ListenableFuture<StoredBlock> future = testNetChain.getHeightFuture(498);
        // Block 1 from the testnet.
        Block b1 = getBlock1(testNet);
        assertTrue(testNetChain.add(b1));
        assertFalse(future.isDone());
        // Block 2 from the testnet.
        Block b2 = getBlock2();

        // Let's try adding an invalid block.
        long n = b2.getNonce();
        try {
            b2.setNonce(12345);
            testNetChain.add(b2);
            fail();
        } catch (VerificationException e) {
            b2.setNonce(n);
        }

        // Now it works because we reset the nonce.
        assertTrue(testNetChain.add(b2));
        assertTrue(future.isDone());
        assertEquals(2, future.get().getHeight());
    }

    // Some blocks from the main net.
    private static Block getBlock2() throws Exception {
        Block b2 = new Block(testNet, 3);
        b2.setMerkleRoot(Sha256Hash.wrap("2c9f161df42d0b1d8cef706125d0af3ed494a172d5cce333b0c2cab72133d275"));
        b2.setNonce(645091202L);
        b2.setTime(1454208376L);
        b2.setPrevBlockHash(Sha256Hash.wrap("00000000018a62703f054b6741d370670a94da204ba794a475d9e60ed0cf1095"));
        assertEquals("0000000003294fe0a280e58595e4673b0c355f9ec65ff17e8c950efd878267fa", b2.getHashAsString());
        b2.verifyHeader();
        return b2;
    }

    private static Block getBlock1(NetworkParameters n) throws Exception {
        Block b1 = new Block(n, 3);
        b1.setMerkleRoot(Sha256Hash.wrap("124c98bde26aacd0e7d995781992584661e44f289631a0323aa827793c9864fb"));
        b1.setNonce(1);
        b1.setTime(1505224801L);
        b1.setDifficultyTarget(0x0);
        b1.setPrevBlockHash(Sha256Hash.wrap("2b1a0f66712aad59ad283662d5b919415a25921ce89511d73019107e380485bf"));

        assertEquals("124c98bde26aacd0e7d995781992584661e44f289631a0323aa827793c9864fb", b1.getHashAsString());
        b1.verifyHeader();
        return b1;
    }

}
