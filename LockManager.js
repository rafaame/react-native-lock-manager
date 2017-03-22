import { NativeModules } from 'react-native';

const LockManager = NativeModules.LockManager;

exports.unlock = function() {  
    LockManager.unlock();
};