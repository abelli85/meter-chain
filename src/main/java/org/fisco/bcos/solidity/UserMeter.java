package org.fisco.bcos.solidity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple5;
import org.fisco.bcos.web3j.tuples.generated.Tuple7;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class UserMeter extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162002902380380620029028339810180604052810190808051820192919060200180518201929190602001805182019291906020018051820192919050505033600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508360029080519060200190620000ad929190620001c1565b508260019080519060200190620000c6929190620001c1565b508160039080519060200190620000df929190620001c1565b508060059080519060200190620000f8929190620001c1565b507fcccdcb0987c1452383f4cccc848dfa4eb3a64b19a921a48de9ad9c2c6501b37160016040518080602001828103825283818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015620001a95780601f106200017d57610100808354040283529160200191620001a9565b820191906000526020600020905b8154815290600101906020018083116200018b57829003601f168201915b50509250505060405180910390a15050505062000270565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200020457805160ff191683800117855562000235565b8280016001018555821562000235579182015b828111156200023457825182559160200191906001019062000217565b5b50905062000244919062000248565b5090565b6200026d91905b80821115620002695760008160009055506001016200024f565b5090565b90565b61268280620002806000396000f30060806040526004361061011d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b929a2d14610122578063176e4635146102615780632ae18dd81461030a5780632b7ac3f31461053a5780633c81ba4f146105915780633ec045a6146106215780634747360c146106785780634972134a146106db5780636af9a8ff1461076b5780636cc6cde1146107c257806374754282146108195780637ac3c02f146108a95780638c72c54e14610a6b5780638e1cf9be14610ac2578063a2161eba14610b25578063a81af5f714610bb5578063b25b1a1314610c45578063bb7b391414610d11578063bef4876b14610d68578063d56b288914610d97578063db486ee614610dae575b600080fd5b34801561012e57600080fd5b5061014d60048036038101908080359060200190929190505050610e05565b60405180846fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156101bd5780820151818401526020810190506101a2565b50505050905090810190601f1680156101ea5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610223578082015181840152602081019050610208565b50505050905090810190601f1680156102505780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561026d57600080fd5b50610308600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f8b565b005b34801561031657600080fd5b5061034860048036038101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506111be565b60405180806020018060200180602001806020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185810385528a818151815260200191508051906020019080838360005b838110156103c65780820151818401526020810190506103ab565b50505050905090810190601f1680156103f35780820380516001836020036101000a031916815260200191505b50858103845289818151815260200191508051906020019080838360005b8381101561042c578082015181840152602081019050610411565b50505050905090810190601f1680156104595780820380516001836020036101000a031916815260200191505b50858103835288818151815260200191508051906020019080838360005b83811015610492578082015181840152602081019050610477565b50505050905090810190601f1680156104bf5780820380516001836020036101000a031916815260200191505b50858103825287818151815260200191508051906020019080838360005b838110156104f85780820151818401526020810190506104dd565b50505050905090810190601f1680156105255780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b34801561054657600080fd5b5061054f6115fd565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561059d57600080fd5b506105a6611623565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105e65780820151818401526020810190506105cb565b50505050905090810190601f1680156106135780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561062d57600080fd5b506106366116c1565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561068457600080fd5b506106d9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506116e7565b005b3480156106e757600080fd5b506106f0611902565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610730578082015181840152602081019050610715565b50505050905090810190601f16801561075d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561077757600080fd5b506107806119a0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156107ce57600080fd5b506107d76119c6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561082557600080fd5b5061082e6119ec565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561086e578082015181840152602081019050610853565b50505050905090810190601f16801561089b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156108b557600080fd5b506108be611a8a565b60405180806020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825289818151815260200191508051906020019080838360005b83811015610a2a578082015181840152602081019050610a0f565b50505050905090810190601f168015610a575780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b348015610a7757600080fd5b50610a80611bf7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b348015610ace57600080fd5b50610b23600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611c1d565b005b348015610b3157600080fd5b50610b3a611e38565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610b7a578082015181840152602081019050610b5f565b50505050905090810190601f168015610ba75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b348015610bc157600080fd5b50610bca611ed6565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610c0a578082015181840152602081019050610bef565b50505050905090810190601f168015610c375780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b348015610c5157600080fd5b50610d0f60048036038101908080356fffffffffffffffffffffffffffffffff19169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611f74565b005b348015610d1d57600080fd5b50610d266121e9565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b348015610d7457600080fd5b50610d7d61220f565b6040","51808215151515815260200191505060405180910390f35b348015610da357600080fd5b50610dac612222565b005b348015610dba57600080fd5b50610dc361250b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b600081815481101515610e1457fe5b90600052602060002090600302016000915090508060000160009054906101000a90047001000000000000000000000000000000000290806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ee35780601f10610eb857610100808354040283529160200191610ee3565b820191906000526020600020905b815481529060010190602001808311610ec657829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f815780601f10610f5657610100808354040283529160200191610f81565b820191906000526020600020905b815481529060010190602001808311610f6457829003601f168201915b5050505050905083565b600660149054906101000a900460ff16151515611010576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe7adbee5908d000081525060200191505060405180910390fd5b8260049080519060200190611026929190612531565b5081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600860006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fe0cc5c65be55719bf41fe1e0cc752ef3972719936d8725c9addd56ba619076356001600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281038252848181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156111aa5780601f1061117f576101008083540402835291602001916111aa565b820191906000526020600020905b81548152906001019060200180831161118d57829003601f168201915b5050935050505060405180910390a1505050565b60608060608060008060008060009250600091505b600080549050821015611501576000828154811015156111ef57fe5b90600052602060002090600302019050886fffffffffffffffffffffffffffffffff19168160000160009054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff191614156114f4576001925060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156112e85780601f106112bd576101008083540402835291602001916112e8565b820191906000526020600020905b8154815290600101906020018083116112cb57829003601f168201915b5050505050975060028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156113855780601f1061135a57610100808354040283529160200191611385565b820191906000526020600020905b81548152906001019060200180831161136857829003601f168201915b50505050509650806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156114245780601f106113f957610100808354040283529160200191611424565b820191906000526020600020905b81548152906001019060200180831161140757829003601f168201915b50505050509550806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156114c35780601f10611498576101008083540402835291602001916114c3565b820191906000526020600020905b8154815290600101906020018083116114a657829003601f168201915b50505050509450600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169350611501565b81806001019250506111d3565b8215156115f1577f6f5346746ac25b0383bab99de398a28d60d57d62de03d0f264e97749bf32633060018a6040518080602001836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020018281038252848181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156115e15780601f106115b6576101008083540402835291602001916115e1565b820191906000526020600020905b8154815290600101906020018083116115c457829003601f168201915b5050935050505060405180910390a15b50505091939590929450565b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156116b95780601f1061168e576101008083540402835291602001916116b9565b820191906000526020600020905b81548152906001019060200180831161169c57829003601f168201915b505050505081565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600660149054906101000a900460ff1615151561176c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe7adbee5908d000081525060200191505060405180910390fd5b81600b60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600c60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fab423536cb68fac8d51c900af66d17f28d133b69f51d949e3228be9ffe0345d96001600b60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281038252848181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156118ef5780601f106118c4576101008083540402835291602001916118ef565b820191906000526020600020905b8154815290600101906020018083116118d257829003601f168201915b5050935050505060405180910390a15050565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156119985780601f1061196d57610100808354040283529160200191611998565b820191906000526020600020905b81548152906001019060200180831161197b57829003601f168201915b505050505081565b600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600b60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60028054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611a825780601f10611a5757610100808354040283529160200191611a82565b820191906000526020600020905b815481529060010190602001808311611a6557829003601f168201915b505050505081565b606060008060008060008060018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611b2b5780601f10611b0057610100808354040283529160200191611b2b565b820191906000526020600020905b815481529060010190602001808311611b0e57829003601f168201915b50505050509650600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169550600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169450600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169350600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169250600b60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16915080905090919293949596565b600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600660149054906101000a900460ff16151515611ca2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe7adbee5908d000081525060200191505060405180910390fd5b81600960006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600a60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fbb94d999153311b5c8082920b0e567dbedbcd8d8f7edddd1cf3e08ab8034ad8d6001600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660405180806020018373ffffffffffffffffffffff","ffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015611e255780601f10611dfa57610100808354040283529160200191611e25565b820191906000526020600020905b815481529060010190602001808311611e0857829003601f168201915b5050935050505060405180910390a15050565b60038054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611ece5780601f10611ea357610100808354040283529160200191611ece565b820191906000526020600020905b815481529060010190602001808311611eb157829003601f168201915b505050505081565b60058054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611f6c5780601f10611f4157610100808354040283529160200191611f6c565b820191906000526020600020905b815481529060010190602001808311611f4f57829003601f168201915b505050505081565b3373ffffffffffffffffffffffffffffffffffffffff16600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561205f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260278152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e68f90e4baa4e6a380e5ae81526020017f9ae7bb93e69e9c0000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600660149054906101000a900460ff1615151561210a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe6b7bbe58aa0e6b081526020017fb4e8a1a8e79a84e6a380e5ae9ae7bb93e69e9c0000000000000000000000000081525060400191505060405180910390fd5b6000606060405190810160405280856fffffffffffffffffffffffffffffffff19168152602001848152602001838152509080600181540180825580915050906001820390600052602060002090600302016000909192909190915060008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff02191690837001000000000000000000000000000000009004021790555060208201518160010190805190602001906121c39291906125b1565b5060408201518160020190805190602001906121e09291906125b1565b50505050505050565b600c60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600660149054906101000a900460ff1681565b3373ffffffffffffffffffffffffffffffffffffffff16600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561230d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e5ae8ce68890e5a794e68981526020017f98e58d950000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600660149054906101000a900460ff161515156123b8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe5868de6aca1e5ae81526020017f8ce688900000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60008080549050111515612434576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807fe7a9bae5a794e68998e58d95e4b88de58fafe4bba5e5ae8ce68890000000000081525060200191505060405180910390fd5b6001600660146101000a81548160ff0219169083151502179055507fd59296f8b36d87ab7d87f8f9e8d60efa2552f52bbf92adf03d6622db07c69a36600160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156124fb5780601f106124d0576101008083540402835291602001916124fb565b820191906000526020600020905b8154815290600101906020018083116124de57829003601f168201915b50509250505060405180910390a1565b600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061257257805160ff19168380011785556125a0565b828001600101855582156125a0579182015b8281111561259f578251825591602001919060010190612584565b5b5090506125ad9190612631565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106125f257805160ff1916838001178555612620565b82800160010185558215612620579182015b8281111561261f578251825591602001919060010190612604565b5b50905061262d9190612631565b5090565b61265391905b8082111561264f576000816000905550600101612637565b5090565b905600a165627a7a7230582073fb79331cabfb2bbc2da017dba3cbd7e13ee416149f75db51c3d7cbe0322f420029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"meterList\",\"outputs\":[{\"name\":\"meterId\",\"type\":\"bytes16\"},{\"name\":\"verifyTime\",\"type\":\"string\"},{\"name\":\"result\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_reportHash\",\"type\":\"string\"},{\"name\":\"_auditor\",\"type\":\"address\"},{\"name\":\"_auditorSigner\",\"type\":\"address\"}],\"name\":\"auditorSign\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"}],\"name\":\"getInfo\",\"outputs\":[{\"name\":\"_batchId\",\"type\":\"string\"},{\"name\":\"_manufacturer\",\"type\":\"string\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"},{\"name\":\"_verifier\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"verifier\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"reportHash\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"auditor\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_arbitrator\",\"type\":\"address\"},{\"name\":\"_arbitratorSigner\",\"type\":\"address\"}],\"name\":\"arbitratorSign\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"batchId\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"auditorSigner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"arbitrator\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"manufacturer\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getSigner\",\"outputs\":[{\"name\":\"_batchId\",\"type\":\"string\"},{\"name\":\"_auditor\",\"type\":\"address\"},{\"name\":\"_auditorSigner\",\"type\":\"address\"},{\"name\":\"_publisher\",\"type\":\"address\"},{\"name\":\"_publisherSigner\",\"type\":\"address\"},{\"name\":\"_arbitrator\",\"type\":\"address\"},{\"name\":\"_arbitratorSigner\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"publisher\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_publisher\",\"type\":\"address\"},{\"name\":\"_publisherSigner\",\"type\":\"address\"}],\"name\":\"publisherSign\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"verifierName\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"validDate\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"verify\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"arbitratorSigner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"finished\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"finish\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"publisherSigner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_manufacturer\",\"type\":\"string\"},{\"name\":\"_batchId\",\"type\":\"string\"},{\"name\":\"_verifierName\",\"type\":\"string\"},{\"name\":\"_validDate\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"}],\"name\":\"batchLaunchedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"_auditor\",\"type\":\"address\"}],\"name\":\"auditorSignedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"_publisher\",\"type\":\"address\"}],\"name\":\"publisherSignedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"_arbitrator\",\"type\":\"address\"}],\"name\":\"arbitratorSignedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"}],\"name\":\"batchFinishedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_batchId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"_meterId\",\"type\":\"bytes16\"}],\"name\":\"meterNotFound\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_METERLIST = "meterList";

    public static final String FUNC_AUDITORSIGN = "auditorSign";

    public static final String FUNC_GETINFO = "getInfo";

    public static final String FUNC_VERIFIER = "verifier";

    public static final String FUNC_REPORTHASH = "reportHash";

    public static final String FUNC_AUDITOR = "auditor";

    public static final String FUNC_ARBITRATORSIGN = "arbitratorSign";

    public static final String FUNC_BATCHID = "batchId";

    public static final String FUNC_AUDITORSIGNER = "auditorSigner";

    public static final String FUNC_ARBITRATOR = "arbitrator";

    public static final String FUNC_MANUFACTURER = "manufacturer";

    public static final String FUNC_GETSIGNER = "getSigner";

    public static final String FUNC_PUBLISHER = "publisher";

    public static final String FUNC_PUBLISHERSIGN = "publisherSign";

    public static final String FUNC_VERIFIERNAME = "verifierName";

    public static final String FUNC_VALIDDATE = "validDate";

    public static final String FUNC_VERIFY = "verify";

    public static final String FUNC_ARBITRATORSIGNER = "arbitratorSigner";

    public static final String FUNC_FINISHED = "finished";

    public static final String FUNC_FINISH = "finish";

    public static final String FUNC_PUBLISHERSIGNER = "publisherSigner";

    public static final Event BATCHLAUNCHEDEVENT_EVENT = new Event("batchLaunchedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event AUDITORSIGNEDEVENT_EVENT = new Event("auditorSignedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event PUBLISHERSIGNEDEVENT_EVENT = new Event("publisherSignedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ARBITRATORSIGNEDEVENT_EVENT = new Event("arbitratorSignedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event BATCHFINISHEDEVENT_EVENT = new Event("batchFinishedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event METERNOTFOUND_EVENT = new Event("meterNotFound", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bytes16>() {}));
    ;

    @Deprecated
    protected UserMeter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected UserMeter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected UserMeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected UserMeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<Tuple3<byte[], String, String>> meterList(BigInteger param0) {
        final Function function = new Function(FUNC_METERLIST, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple3<byte[], String, String>>(
                new Callable<Tuple3<byte[], String, String>>() {
                    @Override
                    public Tuple3<byte[], String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<byte[], String, String>(
                                (byte[]) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> auditorSign(String _reportHash, String _auditor, String _auditorSigner) {
        final Function function = new Function(
                FUNC_AUDITORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_reportHash), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditor), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void auditorSign(String _reportHash, String _auditor, String _auditorSigner, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_AUDITORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_reportHash), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditor), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String auditorSignSeq(String _reportHash, String _auditor, String _auditorSigner) {
        final Function function = new Function(
                FUNC_AUDITORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_reportHash), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditor), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_auditorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<String, String, String> getAuditorSignInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_AUDITORSIGN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue()
                );
    }

    public RemoteCall<Tuple5<String, String, String, String, String>> getInfo(byte[] _meterId) {
        final Function function = new Function(FUNC_GETINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple5<String, String, String, String, String>>(
                new Callable<Tuple5<String, String, String, String, String>>() {
                    @Override
                    public Tuple5<String, String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<String> verifier() {
        final Function function = new Function(FUNC_VERIFIER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> reportHash() {
        final Function function = new Function(FUNC_REPORTHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> auditor() {
        final Function function = new Function(FUNC_AUDITOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> arbitratorSign(String _arbitrator, String _arbitratorSigner) {
        final Function function = new Function(
                FUNC_ARBITRATORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitrator), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitratorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void arbitratorSign(String _arbitrator, String _arbitratorSigner, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ARBITRATORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitrator), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitratorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String arbitratorSignSeq(String _arbitrator, String _arbitratorSigner) {
        final Function function = new Function(
                FUNC_ARBITRATORSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitrator), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_arbitratorSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, String> getArbitratorSignInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ARBITRATORSIGN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public RemoteCall<String> batchId() {
        final Function function = new Function(FUNC_BATCHID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> auditorSigner() {
        final Function function = new Function(FUNC_AUDITORSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> arbitrator() {
        final Function function = new Function(FUNC_ARBITRATOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> manufacturer() {
        final Function function = new Function(FUNC_MANUFACTURER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple7<String, String, String, String, String, String, String>> getSigner() {
        final Function function = new Function(FUNC_GETSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple7<String, String, String, String, String, String, String>>(
                new Callable<Tuple7<String, String, String, String, String, String, String>>() {
                    @Override
                    public Tuple7<String, String, String, String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, String, String, String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (String) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<String> publisher() {
        final Function function = new Function(FUNC_PUBLISHER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> publisherSign(String _publisher, String _publisherSigner) {
        final Function function = new Function(
                FUNC_PUBLISHERSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_publisher), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_publisherSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void publisherSign(String _publisher, String _publisherSigner, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PUBLISHERSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_publisher), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_publisherSigner)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String publisherSignSeq(String _publisher, String _publisherSigner) {
        final Function function = new Function(
                FUNC_PUBLISHERSIGN, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_publisher), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_publisherSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, String> getPublisherSignInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PUBLISHERSIGN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public RemoteCall<String> verifierName() {
        final Function function = new Function(FUNC_VERIFIERNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> validDate() {
        final Function function = new Function(FUNC_VALIDDATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> verify(byte[] _meterId, String _verifyTime, String _result) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void verify(byte[] _meterId, String _verifyTime, String _result, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String verifySeq(byte[] _meterId, String _verifyTime, String _result) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<byte[], String, String> getVerifyInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<byte[], String, String>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue()
                );
    }

    public RemoteCall<String> arbitratorSigner() {
        final Function function = new Function(FUNC_ARBITRATORSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> finished() {
        final Function function = new Function(FUNC_FINISHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> finish() {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void finish(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String finishSeq() {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<String> publisherSigner() {
        final Function function = new Function(FUNC_PUBLISHERSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<BatchLaunchedEventEventResponse> getBatchLaunchedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BATCHLAUNCHEDEVENT_EVENT, transactionReceipt);
        ArrayList<BatchLaunchedEventEventResponse> responses = new ArrayList<BatchLaunchedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BatchLaunchedEventEventResponse typedResponse = new BatchLaunchedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerbatchLaunchedEventEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(BATCHLAUNCHEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerbatchLaunchedEventEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(BATCHLAUNCHEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<AuditorSignedEventEventResponse> getAuditorSignedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUDITORSIGNEDEVENT_EVENT, transactionReceipt);
        ArrayList<AuditorSignedEventEventResponse> responses = new ArrayList<AuditorSignedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuditorSignedEventEventResponse typedResponse = new AuditorSignedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._auditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerauditorSignedEventEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(AUDITORSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerauditorSignedEventEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(AUDITORSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<PublisherSignedEventEventResponse> getPublisherSignedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PUBLISHERSIGNEDEVENT_EVENT, transactionReceipt);
        ArrayList<PublisherSignedEventEventResponse> responses = new ArrayList<PublisherSignedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PublisherSignedEventEventResponse typedResponse = new PublisherSignedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._publisher = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerpublisherSignedEventEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(PUBLISHERSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerpublisherSignedEventEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(PUBLISHERSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<ArbitratorSignedEventEventResponse> getArbitratorSignedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ARBITRATORSIGNEDEVENT_EVENT, transactionReceipt);
        ArrayList<ArbitratorSignedEventEventResponse> responses = new ArrayList<ArbitratorSignedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ArbitratorSignedEventEventResponse typedResponse = new ArbitratorSignedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._arbitrator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerarbitratorSignedEventEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ARBITRATORSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerarbitratorSignedEventEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ARBITRATORSIGNEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<BatchFinishedEventEventResponse> getBatchFinishedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BATCHFINISHEDEVENT_EVENT, transactionReceipt);
        ArrayList<BatchFinishedEventEventResponse> responses = new ArrayList<BatchFinishedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BatchFinishedEventEventResponse typedResponse = new BatchFinishedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerbatchFinishedEventEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(BATCHFINISHEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerbatchFinishedEventEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(BATCHFINISHEDEVENT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<MeterNotFoundEventResponse> getMeterNotFoundEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(METERNOTFOUND_EVENT, transactionReceipt);
        ArrayList<MeterNotFoundEventResponse> responses = new ArrayList<MeterNotFoundEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MeterNotFoundEventResponse typedResponse = new MeterNotFoundEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._batchId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._meterId = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registermeterNotFoundEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(METERNOTFOUND_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registermeterNotFoundEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(METERNOTFOUND_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static UserMeter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserMeter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static UserMeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserMeter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static UserMeter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new UserMeter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static UserMeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new UserMeter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<UserMeter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _manufacturer, String _batchId, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_batchId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<UserMeter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _manufacturer, String _batchId, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_batchId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<UserMeter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _manufacturer, String _batchId, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_batchId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<UserMeter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _manufacturer, String _batchId, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_batchId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class BatchLaunchedEventEventResponse {
        public Log log;

        public String _batchId;
    }

    public static class AuditorSignedEventEventResponse {
        public Log log;

        public String _batchId;

        public String _auditor;
    }

    public static class PublisherSignedEventEventResponse {
        public Log log;

        public String _batchId;

        public String _publisher;
    }

    public static class ArbitratorSignedEventEventResponse {
        public Log log;

        public String _batchId;

        public String _arbitrator;
    }

    public static class BatchFinishedEventEventResponse {
        public Log log;

        public String _batchId;
    }

    public static class MeterNotFoundEventResponse {
        public Log log;

        public String _batchId;

        public byte[] _meterId;
    }
}
